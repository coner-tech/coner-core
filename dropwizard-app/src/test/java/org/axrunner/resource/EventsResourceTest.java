package org.axrunner.resource;

import org.axrunner.api.response.AddEventResponse;
import org.axrunner.api.response.GetEventsResponse;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.core.domain.Event;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EventsResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final AxRunnerCoreService axRunnerCoreService = mock(AxRunnerCoreService.class);

    private EventsResource eventsResource;

    @Before
    public void setup() {
        eventsResource = new EventsResource(eventBoundary, axRunnerCoreService);
    }

    @Test
    public void itShouldGetEvents() {
        List<Event> domainEvents = mock(List.class);
        List<org.axrunner.api.entity.Event> apiEvents = mock(List.class);
        when(axRunnerCoreService.getEvents()).thenReturn(domainEvents);
        when(eventBoundary.toApiEntities(domainEvents)).thenReturn(apiEvents);

        GetEventsResponse response = eventsResource.getEvents();

        verify(axRunnerCoreService).getEvents();
        verify(eventBoundary).toApiEntities(domainEvents);
        assertNotNull(response);
        assertSame(apiEvents, response.getEvents());
    }

    @Test
    public void itShouldAddEvent() {
        org.axrunner.api.entity.Event apiEvent = mock(org.axrunner.api.entity.Event.class);
        Event domainEvent = mock(Event.class);
        when(eventBoundary.toDomainEntity(apiEvent)).thenReturn(domainEvent);
        when(eventBoundary.toApiEntity(domainEvent)).thenReturn(apiEvent);

        AddEventResponse addEventResponse = eventsResource.addEvent(apiEvent);

        verify(axRunnerCoreService).addEvent(domainEvent);
        verify(eventBoundary).toApiEntity(domainEvent);
        assertNotNull(addEventResponse);
        assertSame(apiEvent, addEventResponse.getEvent());
    }
}
