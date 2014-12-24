package org.axrunner.resource;

import org.axrunner.api.entity.Event;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.exception.ResourceException;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EventResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final AxRunnerCoreService axRunnerCoreService = mock(AxRunnerCoreService.class);

    private EventResource eventResource;

    @Before
    public void setup() {
        eventResource = new EventResource(eventBoundary, axRunnerCoreService);
    }

    @Test
    public void itShouldGetEvent() {
        Event apiEvent = mock(Event.class);
        org.axrunner.core.domain.Event domainEvent = mock(org.axrunner.core.domain.Event.class);
        when(eventBoundary.toApiEntity(domainEvent)).thenReturn(apiEvent);
        String id = "test";
        when(axRunnerCoreService.getEvent(id)).thenReturn(domainEvent);

        Event result = eventResource.getEvent(id);

        verify(axRunnerCoreService).getEvent(id);
        assertNotNull(result);
        assertSame(apiEvent, result);
    }

    @Test
    public void whenGetEventIdNotFoundItShouldThrow() {
        String id = "not-found-oh-noes";
        when(axRunnerCoreService.getEvent(id)).thenReturn(null);
        when(eventBoundary.toApiEntity(any(org.axrunner.core.domain.Event.class))).thenReturn(null);

        try {
            eventResource.getEvent(id);
        } catch (Exception e) {
            assertSame(ResourceException.class, e.getClass());
            assertEquals(HttpStatus.NOT_FOUND_404, ((ResourceException) e).getResponse().getStatus());
            return;
        }
        fail("Should have thrown an exception");
    }
}
