package org.axrunner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.axrunner.api.entity.Event;
import org.axrunner.api.response.AddEventResponse;
import org.axrunner.api.response.GetEventsResponse;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 */
@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventsResource {

    private final EventBoundary eventBoundary;
    private final AxRunnerCoreService axRunnerCoreService;

    public EventsResource(EventBoundary eventBoundary, AxRunnerCoreService axRunnerCoreService) {
        this.eventBoundary = eventBoundary;
        this.axRunnerCoreService = axRunnerCoreService;
    }

    @GET
    @UnitOfWork
    public GetEventsResponse getEvents() {
        List<org.axrunner.core.domain.Event> domainEvents = axRunnerCoreService.getEvents();
        GetEventsResponse response = new GetEventsResponse();
        response.setEvents(eventBoundary.toApiEntities(domainEvents));
        return response;
    }

    @POST
    @UnitOfWork
    public AddEventResponse addEvent(@Valid Event event) {
        org.axrunner.core.domain.Event domainEvent = eventBoundary.toDomainEntity(event);
        axRunnerCoreService.addEvent(domainEvent);
        AddEventResponse response = new AddEventResponse();
        response.setEvent(eventBoundary.toApiEntity(domainEvent));
        return response;
    }
}
