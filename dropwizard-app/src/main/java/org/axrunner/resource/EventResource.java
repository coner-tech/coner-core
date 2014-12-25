package org.axrunner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.axrunner.api.entity.Event;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 *
 */
@Path("/events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    private final EventBoundary eventBoundary;
    private final AxRunnerCoreService axRunnerCoreService;

    public EventResource(EventBoundary eventBoundary, AxRunnerCoreService axRunnerCoreService) {
        this.eventBoundary = eventBoundary;
        this.axRunnerCoreService = axRunnerCoreService;
    }

    @GET
    @UnitOfWork
    public Event getEvent(@PathParam("eventId") String id) {
        org.axrunner.core.domain.Event domainEvent = axRunnerCoreService.getEvent(id);
        if (domainEvent == null) {
            throw new NotFoundException("No event found with id " + id);
        }
        Event event = eventBoundary.toApiEntity(domainEvent);
        return event;
    }
}
