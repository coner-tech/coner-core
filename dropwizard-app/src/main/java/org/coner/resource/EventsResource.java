package org.coner.resource;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Event;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Events")
public class EventsResource {

    private final EventBoundary eventBoundary;
    private final ConerCoreService conerCoreService;

    public EventsResource(EventBoundary eventBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a list of all events", response = GetEventsResponse.class)
    public GetEventsResponse getEvents() {
        List<Event> domainEvents = conerCoreService.getEvents();
        GetEventsResponse response = new GetEventsResponse();
        response.setEvents(eventBoundary.toApiEntities(domainEvents));
        return response;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add an Event", response = Response.class)
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    response = Void.class,
                    message = "Created at URI in Location header"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ErrorsResponse.class,
                    message = "Failed validation"
            )
    })
    public Response addEvent(
            @Valid @ApiParam(value = "Event", required = true) EventApiEntity event
    ) {
        Event domainEvent = eventBoundary.toDomainEntity(event);
        conerCoreService.addEvent(domainEvent);
        return Response.created(UriBuilder.fromResource(EventResource.class)
                .build(domainEvent.getId()))
                .build();
    }
}
