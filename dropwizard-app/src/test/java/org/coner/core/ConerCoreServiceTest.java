package org.coner.core;

import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.RegistrationGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.*;

/**
 *
 */
public class ConerCoreServiceTest {

    @Mock
    private EventGateway eventGateway;
    @Mock
    private RegistrationGateway registrationGateway;

    private ConerCoreService conerCoreService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        conerCoreService = new ConerCoreService(
                eventGateway,
                registrationGateway
        );
    }

    @Test
    public void whenGetEventsItShouldGetFromEventGateway() {
        List<Event> expected = new ArrayList<>();
        when(eventGateway.getAll()).thenReturn(expected);

        List<Event> actual = conerCoreService.getEvents();

        verify(eventGateway).getAll();
        assertThat(actual)
                .isSameAs(expected);
    }

    @Test
    public void whenAddEventInstanceItShouldCreate() {
        Event event = mock(Event.class);

        conerCoreService.addEvent(event);

        verify(eventGateway).create(event);
    }

    @Test
    public void whenAddEventNullItShouldNpe() {
        Event event = null;
        try {
            conerCoreService.addEvent(event);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e)
                    .isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(eventGateway);
        }
    }

    @Test
    public void whenGetEventItShouldFindById() {
        String id = "test";
        Event expected = mock(Event.class);
        when(eventGateway.findById(id)).thenReturn(expected);

        Event actual = conerCoreService.getEvent(id);

        verify(eventGateway).findById(id);
        verifyNoMoreInteractions(eventGateway);

        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void whenGetEventWithoutIdItShouldThrow() {
        String[] invalidIds = new String[]{null, ""};
        for (String invalidId : invalidIds) {
            try {
                Event actual = conerCoreService.getEvent(invalidId);
                failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
            } catch (Exception e) {
                assertThat(e).isInstanceOf(IllegalArgumentException.class);
                verifyZeroInteractions(eventGateway);
            }
        }
    }

    @Test
    public void whenGetRegistrationsForEventItShouldGetGetAllWithEvent() {
        Event event = mock(Event.class);
        List<Registration> expected = new ArrayList<>();
        when(registrationGateway.getAllWith(event)).thenReturn(expected);

        List<Registration> actual = conerCoreService.getRegistrations(event);

        verify(registrationGateway).getAllWith(event);
        assertThat(actual).isSameAs(expected);
        verifyNoMoreInteractions(registrationGateway);
    }

    @Test
    public void whenGetRegistrationsForNullItShouldNpe() {
        Event event = null;

        try {
            conerCoreService.getRegistrations(event);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(registrationGateway);
        }
    }

}
