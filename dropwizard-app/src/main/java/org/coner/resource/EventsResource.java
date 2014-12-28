package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Event;
import org.coner.api.response.AddEventResponse;
import org.coner.api.response.GetEventsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;

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
    private final ConerCoreService conerCoreService;

    public EventsResource(EventBoundary eventBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    public GetEventsResponse getEvents() {
        List<org.coner.core.domain.Event> domainEvents = conerCoreService.getEvents();
        GetEventsResponse response = new GetEventsResponse();
        response.setEvents(eventBoundary.toApiEntities(domainEvents));
        return response;
    }

    @POST
    @UnitOfWork
    public AddEventResponse addEvent(@Valid Event event) {
        org.coner.core.domain.Event domainEvent = eventBoundary.toDomainEntity(event);
        conerCoreService.addEvent(domainEvent);
        AddEventResponse response = new AddEventResponse();
        response.setEvent(eventBoundary.toApiEntity(domainEvent));
        return response;
    }
}
