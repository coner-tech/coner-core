package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.response.GetEventRegistrationsResponse;
import org.coner.core.boundary.RegistrationApiAddPayloadBoundary;
import org.coner.core.boundary.RegistrationApiDomainBoundary;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.EventRegistrationService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/events/{eventId}/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
public class EventRegistrationsResource {

    private final EventRegistrationService eventRegistrationService;
    private final RegistrationApiDomainBoundary apiDomainEntityBoundary;
    private final RegistrationApiAddPayloadBoundary addPayloadBoundary;

    @Inject
    public EventRegistrationsResource(
            EventRegistrationService eventRegistrationService,
            RegistrationApiDomainBoundary registrationApiDomainBoundary,
            RegistrationApiAddPayloadBoundary registrationApiAddPayloadBoundary
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.apiDomainEntityBoundary = registrationApiDomainBoundary;
        this.addPayloadBoundary = registrationApiAddPayloadBoundary;
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Get a list of all registrations at an event",
            response = GetEventRegistrationsResponse.class
    )
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.OK_200,
                    message = "Success",
                    response = GetEventRegistrationsResponse.class
            ),
            @ApiResponse(
                    code = HttpStatus.NOT_FOUND_404,
                    message = "No event with given ID",
                    response = ErrorMessage.class
            )
    })
    public GetEventRegistrationsResponse getEventRegistrations(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId
    ) throws EntityNotFoundException {
        List<Registration> domainEntities = eventRegistrationService.getAllWithEventId(eventId);
        GetEventRegistrationsResponse response = new GetEventRegistrationsResponse();
        response.setRegistrations(apiDomainEntityBoundary.toLocalEntities(domainEntities));
        return response;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a new registration")
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    response = Void.class,
                    message = "Created at URI in Location header"
            ),
            @ApiResponse(
                    code = HttpStatus.NOT_FOUND_404,
                    response = ErrorMessage.class,
                    message = "No event with given ID"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ErrorMessage.class,
                    message = "Failed validation"
            )
    })
    public Response addRegistration(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @Valid @ApiParam(value = "Registration", required = true) AddRegistrationRequest request
    ) throws AddEntityException, EntityNotFoundException {
        RegistrationAddPayload addPayload = addPayloadBoundary.toRemoteEntity(request);
        addPayload.eventId = eventId;
        Registration domainEntity = eventRegistrationService.add(addPayload);
        RegistrationApiEntity registration = apiDomainEntityBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(EventRegistrationResource.class)
                .build(eventId, registration.getId()))
                .build();
    }
}
