package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Event;
import org.coner.api.response.GetEventsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;

import com.wordnik.swagger.annotations.*;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * The EventsResource exposes getting and adding Events via the REST API.
 */
@Path("/events")
@Api(value = "/events", description = "Getting or adding an event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventsResource {

    private final EventBoundary eventBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the EventsResource.
     *
     * @param eventBoundary    the EventBoundary to use for converting API and Domain Event entities
     * @param conerCoreService the ConerCoreService
     */
    public EventsResource(EventBoundary eventBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get all events.
     *
     * @return a list of all events
     */
    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a list of all events", response = GetEventsResponse.class, responseContainer = "List")
    public GetEventsResponse getEvents() {
        List<org.coner.core.domain.Event> domainEvents = conerCoreService.getEvents();
        GetEventsResponse response = new GetEventsResponse();
        response.setEvents(eventBoundary.toApiEntities(domainEvents));
        return response;
    }

    /**
     * Add an event.
     *
     * @param event the Event to add
     * @return a response containing the response code and url of the added event
     */
    @POST
    @UnitOfWork
    @ApiOperation(value = "Add an event", response = Response.class)
    public Response addEvent(@ApiParam(value = "Event", required = true) @Valid Event event) {
        org.coner.core.domain.Event domainEvent = eventBoundary.toDomainEntity(event);
        conerCoreService.addEvent(domainEvent);
        return Response.created(UriBuilder.fromResource(EventResource.class)
                .build(domainEvent.getId()))
                .build();
    }
}
