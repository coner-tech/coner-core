package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.boundary.RegistrationApiDomainBoundary;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.service.EventRegistrationService;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/events/{eventId}/registrations/{registrationId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {ApiTagConstants.EVENTS, ApiTagConstants.REGISTRATIONS})
public class EventRegistrationResource {

    private final RegistrationApiDomainBoundary registrationApiDomainBoundary;
    private final EventRegistrationService eventRegistrationService;

    @Inject
    public EventRegistrationResource(
            EventRegistrationService eventRegistrationService,
            RegistrationApiDomainBoundary registrationApiDomainBoundary
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.registrationApiDomainBoundary = registrationApiDomainBoundary;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a specific registration")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = RegistrationApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorMessage.class, message = "Not found"),
            @ApiResponse(
                    code = HttpStatus.CONFLICT_409,
                    response = ErrorMessage.class,
                    message = "Event ID and Registration ID are mismatched"
            )
    })
    public RegistrationApiEntity getRegistration(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @PathParam("registrationId") @ApiParam(value = "Registration ID", required = true) String registrationId
    ) throws EntityMismatchException, EntityNotFoundException {
        Registration domainRegistration = eventRegistrationService.getByEventIdAndRegistrationId(
                eventId,
                registrationId
        );
        return registrationApiDomainBoundary.toLocalEntity(domainRegistration);
    }
}
