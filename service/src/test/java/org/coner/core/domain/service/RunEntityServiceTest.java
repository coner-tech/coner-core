package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.gateway.RunGateway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Before
    public void setup() {
        when(addPayload.getEvent()).thenReturn(addPayloadEvent);
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

}
