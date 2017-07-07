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
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FlatConePenaltyHandicapTimeScoringInteractorTest {

    private FlatConePenaltyHandicapTimeScoringInteractor interactor;

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

    private final BigDecimal runRawTime = BigDecimal.valueOf(100000, 3);;

    @Before
    public void setup() {
        interactor = new FlatConePenaltyHandicapTimeScoringInteractor(TestConstants.EVENT_PENALTY_SECONDS_PER_CONE);
        when(scoredRun.getRun()).thenReturn(run);
        when(run.getRegistration()).thenReturn(registration);
        when(run.getRawTime()).thenReturn(runRawTime);
        when(registration.getHandicapGroup()).thenReturn(handicapGroup);
        when(registration.getCompetitionGroup()).thenReturn(competitionGroup);
        when(handicapGroup.getFactor()).thenReturn(BigDecimal.valueOf(876, 3));
        when(competitionGroup.getFactor()).thenReturn(BigDecimal.ONE);
    }

    @Test
    public void itShouldSetHandicapTimeForCleanRun() {
        when(run.getCones()).thenReturn(0);
        BigDecimal expected = BigDecimal.valueOf(87600, 3);

        interactor.score(scoredRun);

        verify(scoredRun).setHandicapTimeScored(eq(expected));
    }

    @Test
    public void itShouldSetHandicapTimeForConedRun() {
        when(run.getCones()).thenReturn(3);
        BigDecimal expected = BigDecimal.valueOf(93600, 3);

        interactor.score(scoredRun);

        verify(scoredRun).setHandicapTimeScored(eq(expected));
    }

}
