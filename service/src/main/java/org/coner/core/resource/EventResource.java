package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.boundary.EventApiDomainBoundary;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.service.EventEntityService;
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

@Path("/events/{eventId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.EVENTS)
public class EventResource {

    private final EventApiDomainBoundary eventApiDomainBoundary;
    private final EventEntityService eventEntityService;

    @Inject
    public EventResource(EventApiDomainBoundary eventApiDomainBoundary, EventEntityService eventEntityService) {
        this.eventApiDomainBoundary = eventApiDomainBoundary;
        this.eventEntityService = eventEntityService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get an Event")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = EventApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorMessage.class, message = "Not found")
    })
    public EventApiEntity getEvent(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String id
    ) throws EntityNotFoundException {
        Event domainEntity = eventEntityService.getById(id);
        return eventApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
