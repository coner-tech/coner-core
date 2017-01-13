package org.coner.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.service.CompetitionGroupService;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.EventService;
import org.coner.core.domain.service.HandicapGroupService;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.RegistrationService;
import org.coner.util.TestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConerCoreServiceTest {

    @Mock
    private EventService eventService;
    @Mock
    private RegistrationService registrationService;
    @Mock
    private CompetitionGroupService competitionGroupService;
    @Mock
    private CompetitionGroupSetService competitionGroupSetService;
    @Mock
    private HandicapGroupService handicapGroupService;
    @Mock
    private HandicapGroupSetService handicapGroupSetService;

    private ConerCoreService conerCoreService;

    @Before
    public void setup() {
        conerCoreService = new ConerCoreService(
                eventService,
                registrationService,
                competitionGroupService,
                competitionGroupSetService,
                handicapGroupService,
                handicapGroupSetService
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
        EventAddPayload addPayload = mock(EventAddPayload.class);

        conerCoreService.addEvent(addPayload);

        verify(eventService).add(addPayload);
    }

    @Test
    public void whenAddEventNullItShouldNpe() {
        EventAddPayload event = null;
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
    public void whenGetEventItShouldGetById() throws Exception {
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
    public void whenGetRegistrationsForEventItShouldGetGetAllWithEvent() throws Exception {
        String eventId = TestConstants.EVENT_ID;
        List<Registration> expected = new ArrayList<>();
        Event event = mock(Event.class);
        when(eventService.getById(eventId)).thenReturn(event);
        when(registrationService.getAllWith(event)).thenReturn(expected);

        List<Registration> actual = conerCoreService.getRegistrations(eventId);

        verify(eventService).getById(eventId);
        verify(registrationService).getAllWith(event);
        assertThat(actual).isSameAs(expected);
        verifyNoMoreInteractions(registrationService, eventService);
    }

    @Test
    public void whenGetRegistrationsForNullItShouldNpe() {
        String eventId = null;

        try {
            conerCoreService.getRegistrations(eventId);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(registrationService);
        }
    }

    @Test
    public void whenAddHandicapGroupInstanceItShouldCreate() {
        HandicapGroupAddPayload addPayload = mock(HandicapGroupAddPayload.class);

        conerCoreService.addHandicapGroup(addPayload);

        verify(handicapGroupService).add(addPayload);
        verifyNoMoreInteractions(handicapGroupService);
    }

    @Test
    public void whenAddHandicapGroupsAndNullItShouldNpe() {
        HandicapGroupAddPayload addPayload = null;

        try {
            conerCoreService.addHandicapGroup(addPayload);
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(handicapGroupService);
        }
    }

    @Test
    public void whenAddCompetitionGroupInstanceItShouldAdd() {
        CompetitionGroupAddPayload addPayload = mock(CompetitionGroupAddPayload.class);

        conerCoreService.addCompetitionGroup(addPayload);

        verify(competitionGroupService).add(addPayload);
    }

    @Test
    public void whenAddCompetitionGroupsAndNullItShouldNpe() {
        CompetitionGroupAddPayload addPayload = null;

        try {
            conerCoreService.addCompetitionGroup(addPayload);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class);
            verifyZeroInteractions(competitionGroupService);
        }
    }

}
