package org.coner.core.domain.interactor;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.entity.ScoredRun;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RawTimeScoringInteractorTest {

    private RawTimeScoringInteractor interactor;

    @Mock
    ScoredRun scoredRun;
    @Mock
    Run run;
    @Mock
    Event event;

    @Before
    public void setup() {
        interactor = new RawTimeScoringInteractor();
        when(scoredRun.getRun()).thenReturn(run);
        when(run.getRawTime()).thenReturn(TestConstants.RUN_RAW_TIME);
        when(run.getEvent()).thenReturn(event);
        when(event.getConePenaltySeconds()).thenReturn(TestConstants.EVENT_CONE_PENALTY_SECONDS);
    }

    @Test
    public void itShouldSetRawTimeScoredForCleanRun() {
        when(run.getCones()).thenReturn(0);

        interactor.score(scoredRun);

        verify(scoredRun).setRawTimeScored(eq(TestConstants.RUN_RAW_TIME));
    }

    @Test
    public void itShouldSetRawTimeScoredForConedRun() {
        BigDecimal rawTime = BigDecimal.valueOf(100.000D);
        when(run.getRawTime()).thenReturn(rawTime);
        when(run.getCones()).thenReturn(3);
        BigDecimal expected = BigDecimal.valueOf(106.000D);

        interactor.score(scoredRun);

        verify(scoredRun).setRawTimeScored(eq(expected));
    }
}
