package org.coner.resource;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Event;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.jetty.http.HttpStatus;

@Path("/events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Events")
public class EventResource {

    private final EventBoundary eventBoundary;
    private final ConerCoreService conerCoreService;

    public EventResource(EventBoundary eventBoundary, ConerCoreService conerCoreService) {
        this.eventBoundary = eventBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get an Event")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = EventApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public EventApiEntity getEvent(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String id
    ) {
        Event domainEvent = conerCoreService.getEvent(id);
        if (domainEvent == null) {
            throw new NotFoundException("No event found with id " + id);
        }
        EventApiEntity event = eventBoundary.toApiEntity(domainEvent);
        return event;
    }
}
