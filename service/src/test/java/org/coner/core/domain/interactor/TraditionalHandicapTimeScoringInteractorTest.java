package org.coner.core.domain.interactor;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TraditionalHandicapTimeScoringInteractorTest {

    private TraditionalHandicapTimeScoringInteractor interactor;

    @Mock
    ScoredRun scoredRun;
    @Mock
    Run run;
    @Mock
    Registration registration;
    @Mock
    HandicapGroup handicapGroup;
    @Mock
    CompetitionGroup competitionGroup;

    @Before
    public void setup() {
        interactor = new TraditionalHandicapTimeScoringInteractor();
        when(scoredRun.getRun()).thenReturn(run);
        when(run.getRegistration()).thenReturn(registration);
        when(registration.getHandicapGroup()).thenReturn(handicapGroup);
        when(registration.getCompetitionGroup()).thenReturn(competitionGroup);
        when(handicapGroup.getFactor()).thenReturn(BigDecimal.valueOf(876, 3));
        when(competitionGroup.getFactor()).thenReturn(BigDecimal.ONE);
    }

    @Test
    public void itShouldSetHandicapTimeForCleanRun() {
        BigDecimal rawTimeScored = BigDecimal.valueOf(100000, 3);
        when(scoredRun.getRawTimeScored()).thenReturn(rawTimeScored);
        BigDecimal expected = BigDecimal.valueOf(87600, 3);

        interactor.score(scoredRun);

        verify(scoredRun).setHandicapTimeScored(eq(expected));
    }

    @Test
    public void itShouldSetHandicapTimeForConedRun() {
        BigDecimal rawTimeScored = BigDecimal.valueOf(106000, 3);
        when(scoredRun.getRawTimeScored()).thenReturn(rawTimeScored);
        BigDecimal expected = BigDecimal.valueOf(92856, 3);

        interactor.score(scoredRun);

        verify(scoredRun).setHandicapTimeScored(eq(expected));
    }
}
