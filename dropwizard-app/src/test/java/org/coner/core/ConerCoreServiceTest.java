package org.coner.core;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.service.*;
import org.coner.core.gateway.*;

import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConerCoreServiceTest {

    @Mock
    private EventService eventService;
    @Mock
    private RegistrationService registrationService;
    @Mock
    private CompetitionGroupGateway competitionGroupGateway;
    @Mock
    private CompetitionGroupSetGateway competitionGroupSetGateway;
    @Mock
    private HandicapGroupGateway handicapGroupGateway;
    @Mock
    private HandicapGroupSetGateway handicapGroupSetGateway;

    private ConerCoreService conerCoreService;

    @Before
    public void setup() {
        conerCoreService = new ConerCoreService(
                eventService,
                registrationService,
                competitionGroupGateway,
                competitionGroupSetGateway,
                handicapGroupGateway,
                handicapGroupSetGateway
        );
    }

    @Test
    public void whenGetEventsItShouldGetFromEventGateway() {
        List<Event> expected = new ArrayList<>();
        when(eventService.getAll()).thenReturn(expected);

        List<Event> actual = conerCoreService.getEvents();

        verify(eventService).getAll();
        assertThat(actual)
                .isSameAs(expected);
    }

    @Test
    public void whenAddEventInstanceItShouldCreate() {
        Event event = mock(Event.class);

        conerCoreService.addEvent(event);

        verify(eventService).add(event);
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
            verifyZeroInteractions(eventService);
        }
    }

    @Test
    public void whenGetEventItShouldGetById() {
        String id = "test";
        Event expected = mock(Event.class);
        when(eventService.getById(id)).thenReturn(expected);

        Event actual = conerCoreService.getEvent(id);

        verify(eventService).getById(id);
        verifyNoMoreInteractions(eventService);

        assertThat(actual).isSameAs(expected);
    }

    @Test
    public void whenGetEventWithNullIdItShouldThrow() {
        String nullId = null;
        try {
            Event actual = conerCoreService.getEvent(nullId);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(eventService);
        }
    }

    @Test
    public void whenGetRegistrationsForEventItShouldGetGetAllWithEvent() {
        Event event = mock(Event.class);
        List<Registration> expected = new ArrayList<>();
        when(registrationService.getAllWith(event)).thenReturn(expected);

        List<Registration> actual = conerCoreService.getRegistrations(event);

        verify(registrationService).getAllWith(event);
        assertThat(actual).isSameAs(expected);
        verifyNoMoreInteractions(registrationService);
    }

    @Test
    public void whenGetRegistrationsForNullItShouldNpe() {
        Event event = null;

        try {
            conerCoreService.getRegistrations(event);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(registrationService);
        }
    }

    @Test
    public void whenAddHandicapGroupInstanceItShouldCreate() {
        HandicapGroup handicapGroup = mock(HandicapGroup.class);

        conerCoreService.addHandicapGroup(handicapGroup);

        verify(handicapGroupGateway).create(handicapGroup);
    }

    @Test
    public void whenAddHandicapGroupsAndNullItShouldNpe() {
        HandicapGroup handicapGroup = null;

        try {
            conerCoreService.addHandicapGroup(handicapGroup);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(handicapGroupGateway);
        }
    }

    @Test
    public void whenAddCompetitionGroupInstanceItShouldCreate() {
        CompetitionGroup competitionGroup = mock(CompetitionGroup.class);

        conerCoreService.addCompetitionGroup(competitionGroup);

        verify(competitionGroupGateway).create(competitionGroup);
    }

    @Test
    public void whenAddCompetitionGroupsAndNullItShouldNpe() {
        CompetitionGroup competitionGroup = null;

        try {
            conerCoreService.addCompetitionGroup(competitionGroup);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(competitionGroupGateway);
        }
    }

}
