package org.coner.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventRegistrationServiceTest {

    @InjectMocks
    EventRegistrationService eventRegistrationService;

    @Mock
    EventEntityService eventEntityService;
    @Mock
    RegistrationEntityService registrationEntityService;

    @Test
    public void whenGetAllWithEventIdValid() throws Exception {
        Event event = mock(Event.class);
        List<Registration> registrations = mock(List.class);
        when(eventEntityService.getById(TestConstants.EVENT_ID)).thenReturn(event);
        when(registrationEntityService.getAllWith(event)).thenReturn(registrations);

        List<Registration> actual = eventRegistrationService.getAllWithEventId(TestConstants.EVENT_ID);

        verify(eventEntityService).getById(TestConstants.EVENT_ID);
        verify(registrationEntityService).getAllWith(event);
        assertThat(actual).isSameAs(registrations);
    }

    @Test
    public void whenGetByEventIdAndRegistrationIdValid() throws Exception {
        Event event = mock(Event.class);
        Registration registration = mock(Registration.class);
        when(registration.getEvent()).thenReturn(event);
        when(event.getId()).thenReturn(TestConstants.EVENT_ID);
        when(eventEntityService.getById(TestConstants.EVENT_ID)).thenReturn(event);
        when(registrationEntityService.getById(TestConstants.REGISTRATION_ID)).thenReturn(registration);

        Registration actual = eventRegistrationService.getByEventIdAndRegistrationId(
                TestConstants.EVENT_ID,
                TestConstants.REGISTRATION_ID
        );

        verify(eventEntityService).getById(TestConstants.EVENT_ID);
        verify(registrationEntityService).getById(TestConstants.REGISTRATION_ID);
        assertThat(actual).isSameAs(registration);
    }

    @Test
    public void whenAddValid() throws Exception {
        RegistrationAddPayload registrationAddPayload = new RegistrationAddPayload();
        registrationAddPayload.setEventId(TestConstants.EVENT_ID);
        Registration registration = mock(Registration.class);
        when(registrationEntityService.add(registrationAddPayload)).thenReturn(registration);

        Registration actual = eventRegistrationService.add(registrationAddPayload);

        verify(eventEntityService).getById(TestConstants.EVENT_ID);
        verify(registrationEntityService).add(registrationAddPayload);
        assertThat(actual).isSameAs(registration);
    }

}
