package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Registration;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.exception.EventRegistrationMismatchException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * The EventRegistrationResource exposes getting, updating, or deleting a
 * Registration for an Event via the REST API.
 */
@Path("/events/{eventId}/registrations/{registrationId}")
@Api(value = "/events/{eventId}/registrations", description = "Getting, updating, or deleting registrations")
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
     * @param eventId        the id of the Event to get
     * @param registrationId the id of the Registration
     * @return the Event with the id
     * @throws javax.ws.rs.NotFoundException if no Event is found having eventId, or if no Registration is found having
     *                                       registrationId
     */
    @GET
    @ApiOperation(value = "Get a specific registration",
                  notes = "Requires eventId and registrationId",
                  response = Registration.class)
    @UnitOfWork
    public Response getRegistration(@ApiParam(value = "Event ID", required = true)
                                    @PathParam("eventId") String eventId,
                                    @ApiParam(value = "Registration ID", required = true)                                    
                                    @PathParam("registrationId") String registrationId) {
        org.coner.core.domain.Registration domainRegistration;
        try {
            domainRegistration = conerCoreService.getRegistration(
                    eventId,
                    registrationId
            );
        } catch (EventRegistrationMismatchException e) {
            ErrorsResponse errorsResponse = new ErrorsResponse();
            errorsResponse.setErrors(Arrays.asList(
                    Response.Status.CONFLICT.getReasonPhrase(),
                    e.getMessage()
            ));
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(errorsResponse)
                    .build();
        }
        if (domainRegistration == null) {
            throw new NotFoundException("No registration with id " + registrationId);
        }

        Registration registration = registrationBoundary.toApiEntity(domainRegistration);

        return Response.ok(registration, MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
