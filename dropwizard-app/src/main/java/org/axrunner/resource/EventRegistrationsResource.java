package org.axrunner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.axrunner.api.entity.Registration;
import org.axrunner.api.response.GetEventRegistrationsResponse;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.boundary.RegistrationBoundary;
import org.axrunner.core.AxRunnerCoreService;

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
    private final AxRunnerCoreService axRunnerCoreService;

    public EventRegistrationsResource(EventBoundary eventBoundary, RegistrationBoundary registrationBoundary, AxRunnerCoreService axRunnerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.axRunnerCoreService = axRunnerCoreService;
    }

    @GET
    @UnitOfWork
    public GetEventRegistrationsResponse getEventRegistrations(@PathParam("eventId") String eventId) {
        org.axrunner.core.domain.Event domainEvent = axRunnerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        List<Registration> registrations = registrationBoundary.toApiEntities(axRunnerCoreService.getRegistrations(domainEvent));
        GetEventRegistrationsResponse response = new GetEventRegistrationsResponse();
        response.setRegistrations(registrations);
        return response;
    }
}
