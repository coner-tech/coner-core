package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.Registration;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.boundary.RegistrationBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.exception.EventRegistrationMismatchException;
import org.eclipse.jetty.http.HttpStatus;

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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
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
    @UnitOfWork
    @ApiOperation(value = "Get a specific registration")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = Registration.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found"),
            @ApiResponse(
                    code = HttpStatus.CONFLICT_409,
                    response = ErrorsResponse.class,
                    message = "Event ID and Registration ID are mismatched"
            )
    })
    public Response getRegistration(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @PathParam("registrationId") @ApiParam(value = "Registration ID", required = true) String registrationId
    ) {
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
