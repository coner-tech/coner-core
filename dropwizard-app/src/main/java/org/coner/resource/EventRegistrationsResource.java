package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.request.AddRegistrationRequest;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.exception.EntityNotFoundException;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events/{eventId}/registrations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Event Registrations")
public class EventRegistrationsResource {

    private final ConerCoreService conerCoreService;
    private final RegistrationApiDomainBoundary apiDomainEntityBoundary;
    private final RegistrationApiAddPayloadBoundary addPayloadBoundary;

    public EventRegistrationsResource(
            ConerCoreService conerCoreService,
            RegistrationApiDomainBoundary registrationApiDomainBoundary,
            RegistrationApiAddPayloadBoundary registrationApiAddPayloadBoundary
    ) {
        this.conerCoreService = conerCoreService;
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
                    response = ErrorsResponse.class
            )
    })
    public GetEventRegistrationsResponse getEventRegistrations(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId
    ) {
        List<Registration> domainEntities;
        try {
            domainEntities = conerCoreService.getRegistrations(eventId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
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
                    response = ErrorsResponse.class,
                    message = "No event with given ID"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ErrorsResponse.class,
                    message = "Failed validation"
            )
    })
    public Response addRegistration(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @Valid @ApiParam(value = "Registration", required = true) AddRegistrationRequest request
    ) {
        RegistrationAddPayload addPayload = addPayloadBoundary.toRemoteEntity(request);
        addPayload.eventId = eventId;
        Registration domainEntity = null;
        try {
            domainEntity = conerCoreService.addRegistration(addPayload);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
        RegistrationApiEntity registration = apiDomainEntityBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(EventRegistrationResource.class)
                .build(eventId, registration.getId()))
                .build();
    }
}
