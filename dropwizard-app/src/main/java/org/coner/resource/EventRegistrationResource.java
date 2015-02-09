package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Registration;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The EventRegistrationResource exposes getting, updating, or deleting a
 * Registration for an Event via the REST API.
 */
@Path("/events/{eventId}/registrations/{registrationId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventRegistrationResource {

    private final EventBoundary eventBoundary;
    private final RegistrationBoundary registrationBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the EventRegistrationResource.
     *
     * @param eventBoundary        the EventBoundary to use for converting API and Domain Event entities
     * @param registrationBoundary the RegistrationBoundary to use for converting API and Domain Registration entities
     * @param conerCoreService     the conerCoreService
     */
    public EventRegistrationResource(
            EventBoundary eventBoundary,
            RegistrationBoundary registrationBoundary,
            ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get a Registration.
     *
     * @param eventId the id of the Event to get
     * @param registrationId the id of the Registration
     * @return the Event with the id
     * @throws javax.ws.rs.NotFoundException if no Event is found having the id
     */
    @GET
    @UnitOfWork
    public Registration getRegistration(@PathParam("eventId") String eventId,
                                        @PathParam("registrationId") String registrationId) {
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        Registration registration = registrationBoundary.toApiEntity(
                conerCoreService.getRegistration(registrationId));
        return registration;
    }
}
