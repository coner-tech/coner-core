package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.interactor.RunScoringInteractor;
import org.coner.core.domain.payload.GetRegistrationResultsPayload;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResultsServiceTest {

    @InjectMocks
    ResultsService resultsService;

    @Mock
    RunEntityService runEntityService;
    @Mock
    RunScoringInteractor runScoringInteractor;

    @Mock
    Registration registration;
    @Mock
    Event event;

    List<Run> allRunsWithRegistration = new ArrayList<>();

    @Before
    public void setup() {
        allRunsWithRegistration.clear();
        when(runEntityService.getAllWithRegistration(registration)).thenReturn(allRunsWithRegistration);
        when(registration.getEvent()).thenReturn(event);
        when(event.getMaxRunsPerRegistration()).thenReturn(2);
    }

    @Test
    public void itShouldGetResultsWithoutScoreForRegistrationWithNoRuns() {
        GetRegistrationResultsPayload actual = resultsService.getResultsFor(registration);

        assertThat(actual).extracting(
                GetRegistrationResultsPayload::getRegistration,
                GetRegistrationResultsPayload::getScore
        ).containsExactly(registration, null);
        verifyZeroInteractions(runScoringInteractor);
    }

    @Test
    public void itShouldGetResultsWithScoreForRegistrationWithCompletedRuns() {
        ScoredRun expectedScoredRun = DomainEntityTestUtils.fullScoredRun();
        allRunsWithRegistration.addAll(Arrays.asList(mock(Run.class), mock(Run.class)));
        expectedScoredRun.setRawTimeScored(TestConstants.SCORED_RUN_RAW_TIME_SCORED.subtract(BigDecimal.ONE));
        expectedScoredRun.setHandicapTimeScored(TestConstants.SCORED_RUN_HANDICAP_TIME_SCORED.subtract(BigDecimal.ONE));
        ScoredRun unexpectedScoredRun = DomainEntityTestUtils.fullScoredRun();
        unexpectedScoredRun.setRawTimeScored(TestConstants.SCORED_RUN_RAW_TIME_SCORED.add(BigDecimal.ONE));
        unexpectedScoredRun.setHandicapTimeScored(TestConstants.SCORED_RUN_HANDICAP_TIME_SCORED.add(BigDecimal.ONE));
        List<ScoredRun> expectedScoredRuns = Arrays.asList(unexpectedScoredRun, expectedScoredRun);
        when(runScoringInteractor.score(any(Run.class))).thenReturn(unexpectedScoredRun, expectedScoredRun);

        GetRegistrationResultsPayload actual = resultsService.getResultsFor(registration);

        assertThat(actual.getScore()).isEqualTo(expectedScoredRun);
        assertThat(actual.getScoredRuns()).isEqualTo(expectedScoredRuns);
        assertThat(actual.getRegistration()).isSameAs(registration);
    }

    @Test
    public void whenGetResultsForRegistrationItShouldNotScoreRunsBeyondMaxPerRegistration() {
        when(event.getMaxRunsPerRegistration()).thenReturn(1);
        allRunsWithRegistration.addAll(Arrays.asList(mock(Run.class), mock(Run.class)));
        ScoredRun expectedScoredRun = mock(ScoredRun.class);
        when(runScoringInteractor.score(any())).thenReturn(expectedScoredRun);

        GetRegistrationResultsPayload actual = resultsService.getResultsFor(registration);

        assertThat(actual.getScoredRuns()).hasSize(1);
        assertThat(actual.getScore()).isSameAs(expectedScoredRun);
        verify(runScoringInteractor, Mockito.times(1)).score(any(Run.class));
    }
}
