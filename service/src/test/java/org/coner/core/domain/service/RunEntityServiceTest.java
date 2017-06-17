package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.payload.RunAddTimePayload;
import org.coner.core.domain.payload.RunTimeAddedPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.gateway.RunGateway;
import org.coner.core.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RunEntityServiceTest {

    @InjectMocks
    RunEntityService service;

    @Mock
    RunGateway gateway;

    @Mock
    RunAddPayload addPayload;
    @Mock
    Event addPayloadEvent;
    @Mock
    Run addedRun;

    @Mock
    RunAddTimePayload runAddRawTimePayload;
    @Mock
    Event runAddRawTimePayloadEvent;
    @Mock
    BigDecimal runAddRawTimePayloadRawTime;

    @Before
    public void setup() {
        when(addPayload.getEvent()).thenReturn(addPayloadEvent);
        when(runAddRawTimePayload.getEvent()).thenReturn(runAddRawTimePayloadEvent);
        when(runAddRawTimePayload.getRawTime()).thenReturn(runAddRawTimePayloadRawTime);
    }

    @Test
    public void whenAddRunWithEventNoPriorRunsItShouldSetSequence() throws AddEntityException {
        when(gateway.findLastInSequenceForEvent(addPayloadEvent)).thenReturn(null);
        when(gateway.add(addPayload)).thenReturn(addedRun);

        Run actual = service.add(addPayload);

        verify(addPayload).setSequence(1);
        verify(gateway).add(addPayload);
        assertThat(actual).isSameAs(addedRun);
    }

    @Test
    public void whenAddRunWithEventManyPriorRunsItShouldSetSequence() throws AddEntityException {
        Run lastInSequenceForEvent = mock(Run.class);
        when(gateway.findLastInSequenceForEvent(addPayloadEvent)).thenReturn(lastInSequenceForEvent);
        when(lastInSequenceForEvent.getSequence()).thenReturn(600);
        when(gateway.add(addPayload)).thenReturn(addedRun);

        Run actual = service.add(addPayload);

        verify(addPayload).setSequence(601);
        verify(gateway).add(addPayload);
        assertThat(actual).isSameAs(addedRun);
    }

    @Test
    public void whenAddToFirstRunInSequenceWithoutRawTimeWithRunLackingTime()
            throws AddEntityException, EntityNotFoundException {
        Run firstRunInSequenceWithoutRawTime = mock(Run.class);
        when(firstRunInSequenceWithoutRawTime.getId()).thenReturn(TestConstants.RUN_ID);
        when(gateway.findFirstInSequenceWithoutTime(runAddRawTimePayloadEvent))
                .thenReturn(firstRunInSequenceWithoutRawTime);
        Run runWithRawTimeAssigned = mock(Run.class);
        when(gateway.save(TestConstants.RUN_ID, firstRunInSequenceWithoutRawTime)).thenReturn(runWithRawTimeAssigned);

        RunTimeAddedPayload actual = service.addTimeToFirstRunInSequenceWithoutRawTime(runAddRawTimePayload);

        verify(firstRunInSequenceWithoutRawTime).setRawTime(runAddRawTimePayloadRawTime);
        verify(gateway).save(TestConstants.RUN_ID, firstRunInSequenceWithoutRawTime);
        assertThat(actual.getOutcome()).isEqualTo(RunTimeAddedPayload.Outcome.RUN_RAWTIME_ASSIGNED_TO_EXISTING);
        assertThat(actual.getRun()).isSameAs(runWithRawTimeAssigned);
    }

    @Test
    public void whenAddToFirstRunInSequenceWithoutRawTimeButNoRunsLackRawTime()
            throws AddEntityException, EntityNotFoundException {
        when(gateway.findFirstInSequenceWithoutTime(runAddRawTimePayloadEvent)).thenReturn(null);
        ArgumentCaptor<RunAddPayload> runAddPayloadCaptor = ArgumentCaptor.forClass(RunAddPayload.class);
        Run addedRun = mock(Run.class);
        when(gateway.add(any(RunAddPayload.class))).thenReturn(addedRun);

        RunTimeAddedPayload actual = service.addTimeToFirstRunInSequenceWithoutRawTime(runAddRawTimePayload);

        verify(gateway).add(runAddPayloadCaptor.capture());
        RunAddPayload runAddPayload = runAddPayloadCaptor.getValue();
        assertThat(runAddPayload.getEvent()).isSameAs(runAddRawTimePayloadEvent);
        assertThat(runAddPayload.getRawTime()).isSameAs(runAddRawTimePayloadRawTime);
        assertThat(runAddPayload.getTimestamp()).isBetween(Instant.now().minusSeconds(1), Instant.now());
        assertThat(actual.getOutcome()).isEqualTo(RunTimeAddedPayload.Outcome.RUN_ADDED_WITH_RAWTIME);
        assertThat(actual.getRun()).isSameAs(addedRun);
    }

}
