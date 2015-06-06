package org.coner.resource;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Registration;

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

    private final EventBoundary eventBoundary;
    private final RegistrationBoundary registrationBoundary;
    private final ConerCoreService conerCoreService;

    public EventRegistrationsResource(
            EventBoundary eventBoundary,
            RegistrationBoundary registrationBoundary,
            ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.registrationBoundary = registrationBoundary;
        this.conerCoreService = conerCoreService;
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
        org.coner.core.domain.Event domainEvent = conerCoreService.getEvent(eventId);
        if (domainEvent == null) {
            throw new NotFoundException("No event with id " + eventId);
        }
        List<RegistrationApiEntity> registrations = registrationBoundary.toApiEntities(
                conerCoreService.getRegistrations(domainEvent)
        );
        GetEventRegistrationsResponse response = new GetEventRegistrationsResponse();
        response.setRegistrations(registrations);
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
            @Valid @ApiParam(value = "Registration", required = true) RegistrationApiEntity registration
    ) {
        Registration domainRegistration = registrationBoundary.toDomainEntity(registration);
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
