package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Registration;
import org.coner.core.exception.EventRegistrationMismatchException;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.Arrays;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events/{eventId}/registrations/{registrationId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
public class EventRegistrationResource {

    private final EventBoundary eventBoundary;
    private final RegistrationBoundary registrationBoundary;
    private final ConerCoreService conerCoreService;

    public EventRegistrationResource(
            EventBoundary eventBoundary,
            RegistrationBoundary registrationBoundary,
            ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a specific registration")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = RegistrationApiEntity.class, message = "OK"),
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
        Registration domainRegistration;
        try {
            domainRegistration = conerCoreService.getRegistration(eventId, registrationId);
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

        RegistrationApiEntity registration = registrationBoundary.toApiEntity(domainRegistration);

        return Response.ok(registration, MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
