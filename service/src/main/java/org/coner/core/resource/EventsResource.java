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

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.response.GetEventsResponse;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.service.EventEntityService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.EventMapper;
import org.coner.core.util.swagger.ApiResponseConstants;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.EVENTS)
public class EventsResource {

    private final EventEntityService eventEntityService;
    private final EventMapper eventMapper;

    @Inject
    public EventsResource(
            EventEntityService eventEntityService,
            EventMapper eventMapper
    ) {
        this.eventEntityService = eventEntityService;
        this.eventMapper = eventMapper;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a list of all events", response = GetEventsResponse.class)
    public GetEventsResponse getEvents() {
        List<Event> domainEvents = eventEntityService.getAll();
        GetEventsResponse response = new GetEventsResponse();
        response.setEntities(eventMapper.toApiEntityList(domainEvents));
        return response;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add an Event", response = Response.class)
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    message = ApiResponseConstants.Created.MESSAGE,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = ApiResponseConstants.Created.Headers.NAME,
                                    description = ApiResponseConstants.Created.Headers.DESCRIPTION,
                                    response = String.class
                            )
                    }
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ValidationErrorMessage.class,
                    message = "Failed validation"
            )
    })
    public Response addEvent(
            @Valid @ApiParam(value = "Event", required = true) AddEventRequest request
    ) throws AddEntityException {
        EventAddPayload addPayload = eventMapper.toDomainAddPayload(request);
        Event domainEntity = eventEntityService.add(addPayload);
        EventApiEntity eventApiEntity = eventMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromMethod(EventsResource.class, "getEvent")
                .build(eventApiEntity.getId()))
                .build();
    }

    @GET
    @Path("/{eventId}")
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
        return eventMapper.toApiEntity(domainEntity);
    }
}
