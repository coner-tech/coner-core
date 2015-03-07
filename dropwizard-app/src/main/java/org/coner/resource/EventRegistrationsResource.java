package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Registration;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * The EventRegistrationsResource exposes getting Registrations for an Event
 * or adding a Registration for an Event via the REST API.
 */
@Path("/events/{eventId}/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventRegistrationsResource {

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
    public EventRegistrationsResource(
            EventBoundary eventBoundary,
            RegistrationBoundary registrationBoundary,
            ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get all Registrations for an Event.
     *
     * @param eventId the id of the Event to get Registrations
     * @return a response containing a list of all Registrations
     * @throws javax.ws.rs.NotFoundException if no Event is found having the id
     */
    @GET
    @UnitOfWork
    public GetEventRegistrationsResponse getEventRegistrations(@PathParam("eventId") String eventId) {
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        List<Registration> registrations = registrationBoundary.toApiEntities(
                conerCoreService.getRegistrations(domainEvent)
        );
        GetEventRegistrationsResponse response = new GetEventRegistrationsResponse();
        response.setRegistrations(registrations);
        return response;
    }

    /**
     * Add a new Registration for an Event.
     *
     * @param eventId the id of the Event to get Registrations
     * @param registration the Registration to add
     * @return a response containing response code and url of the added registration
     * @throws javax.ws.rs.NotFoundException if no Event is found having the id
     */
    @POST
    @UnitOfWork
    public Response addRegistration(@PathParam("eventId") String eventId,
                                    @Valid Registration registration) {
        org.coner.core.domain.Registration domainRegistration = registrationBoundary.toDomainEntity(registration);
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        conerCoreService.addRegistration(domainEvent, domainRegistration);
        return Response.created(UriBuilder.fromResource(EventRegistrationResource.class)
                .build(eventId, domainRegistration.getId()))
                .build();
    }
}
