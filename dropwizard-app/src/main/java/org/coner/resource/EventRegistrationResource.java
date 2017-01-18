package org.coner.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.RegistrationApiDomainBoundary;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.service.RegistrationEntityService;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/events/{eventId}/registrations/{registrationId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
public class EventRegistrationResource {

    private final RegistrationApiDomainBoundary registrationApiDomainBoundary;
    private final RegistrationEntityService registrationEntityService;

    @Inject
    public EventRegistrationResource(
            RegistrationEntityService registrationEntityService,
            RegistrationApiDomainBoundary registrationApiDomainBoundary
    ) {
        this.registrationApiDomainBoundary = registrationApiDomainBoundary;
        this.registrationEntityService = registrationEntityService;
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
            domainRegistration = registrationEntityService.getByEventIdAndRegistrationId(eventId, registrationId);
        } catch (EntityMismatchException e) {
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(
                            new ErrorsResponse(
                                    Response.Status.CONFLICT.getReasonPhrase(),
                                    e.getMessage()
                            )
                    )
                    .build();
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No registration with id " + registrationId);
        }

        RegistrationApiEntity registration = registrationApiDomainBoundary.toLocalEntity(domainRegistration);

        return Response.ok(registration, MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
