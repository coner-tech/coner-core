package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Event;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The EventResource exposes getting, updating, and deleting an Event via the REST API.
 */
@Path("/events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    private final EventBoundary eventBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the EventResource.
     *
     * @param eventBoundary    the EventBoundary to use for converting API and Domain Event entities
     * @param conerCoreService the ConerCoreService
     */
    public EventResource(EventBoundary eventBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get an Event.
     *
     * @param id the id of the Event to get
     * @return the Event with the id
     * @throws javax.ws.rs.NotFoundException if no Event is found having the id
     */
    @GET
    @UnitOfWork
    public Event getEvent(@PathParam("eventId") String id) {
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(id);
        if (domainEvent == null) {
            throw new NotFoundException("No event found with id " + id);
        }
        Event event = eventBoundary.toApiEntity(domainEvent);
        return event;
    }
}
