package org.coner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.EventApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Event;
import org.coner.core.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Events")
public class EventResource {

    private final EventApiDomainBoundary eventApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public EventResource(EventApiDomainBoundary eventApiDomainBoundary, ConerCoreService conerCoreService) {
        this.eventApiDomainBoundary = eventApiDomainBoundary;
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
        Event domainEntity = null;
        try {
            domainEntity = conerCoreService.getEvent(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No event found with id " + id);
        }
        return eventApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
