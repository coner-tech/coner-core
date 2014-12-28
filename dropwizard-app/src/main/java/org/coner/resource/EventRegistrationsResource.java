package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Registration;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 */
@Path("/events/{eventId}/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventRegistrationsResource {

    private final EventBoundary eventBoundary;
    private final RegistrationBoundary registrationBoundary;
    private final ConerCoreService conerCoreService;

    public EventRegistrationsResource(EventBoundary eventBoundary, RegistrationBoundary registrationBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    public GetEventRegistrationsResponse getEventRegistrations(@PathParam("eventId") String eventId) {
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        List<Registration> registrations = registrationBoundary.toApiEntities(conerCoreService.getRegistrations(domainEvent));
        GetEventRegistrationsResponse response = new GetEventRegistrationsResponse();
        response.setRegistrations(registrations);
        return response;
    }
}
