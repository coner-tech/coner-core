package org.coner.core.domain.interactor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.domain.value.HandicapTimeScoringMethod;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RunScoringInteractorTest {

    private RunScoringInteractor interactor;

    @Mock
    RawTimeScoringInteractor rawTimeScoringInteractor;
    @Mock
    HandicapTimeScoringInteractor handicapTimeScoringInteractor;

    @Mock
    Run run;
    @Mock
    Event event;

    @Before
    public void setup() {
        Map<HandicapTimeScoringMethod, HandicapTimeScoringInteractor> handicapTimeScoringInteractors = new HashMap<>();
        handicapTimeScoringInteractors.put(
                HandicapTimeScoringMethod.UNSCALED_CONE_PENALTY,
                handicapTimeScoringInteractor
        );

        interactor = new RunScoringInteractor(rawTimeScoringInteractor, handicapTimeScoringInteractors);
    }

    @Test
    public void itShouldNotScoreNullRun() {
        run = null;

        ScoredRun actual = interactor.score(run);

        assertThat(actual).isNull();
        verifyZeroInteractions(rawTimeScoringInteractor, handicapTimeScoringInteractor);
    }

    @Test
    public void itShouldNotScoreRunWithoutRawTime() {
        when(run.getRawTime()).thenReturn(null);

        ScoredRun actual = interactor.score(run);

        assertThat(actual).isNull();
        verifyZeroInteractions(rawTimeScoringInteractor, handicapTimeScoringInteractor);
    }

    @Test
    public void itShouldNotScoreRunWithRerun() {
        when(run.getRawTime()).thenReturn(TestConstants.RUN_RAW_TIME);
        when(run.isRerun()).thenReturn(true);

        ScoredRun actual = interactor.score(run);

        assertThat(actual).isNull();
        verifyZeroInteractions(rawTimeScoringInteractor, handicapTimeScoringInteractor);
    }

    @Test
    public void itShouldNotScoreRunWithoutCompetitive() {
        when(run.getRawTime()).thenReturn(TestConstants.RUN_RAW_TIME);
        when(run.isRerun()).thenReturn(false);
        when(run.isCompetitive()).thenReturn(false);

        ScoredRun actual = interactor.score(run);

        assertThat(actual).isNull();
        verifyZeroInteractions(rawTimeScoringInteractor, handicapTimeScoringInteractor);
    }

    @Test
    public void itShouldScoreRun() {
        run = DomainEntityTestUtils.fullRun();

        ScoredRun actual = interactor.score(run);

        verify(rawTimeScoringInteractor).score(actual);
        verify(handicapTimeScoringInteractor).score(actual);
        assertThat(actual.getRun()).isSameAs(run);
    }

}
