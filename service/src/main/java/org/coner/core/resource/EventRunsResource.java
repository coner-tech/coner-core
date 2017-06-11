package org.coner.core.resource;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.domain.service.RunEntityService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.RunMapper;
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

@Path("/events/{eventId}/runs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {ApiTagConstants.EVENTS, ApiTagConstants.RUNS})
public class EventRunsResource {

    private final RunEntityService runEntityService;
    private final RunMapper runMapper;

    @Inject
    public EventRunsResource(RunEntityService runEntityService, RunMapper runMapper) {
        this.runEntityService = runEntityService;
        this.runMapper = runMapper;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a new run")
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
                    code = HttpStatus.NOT_FOUND_404,
                    response = ErrorMessage.class,
                    message = "No event with given ID"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    response = ValidationErrorMessage.class,
                    message = "Failed validation"
            )
    })
    public Response addRun(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @Valid @ApiParam(value = "Run", required = true) AddRunRequest request
    ) throws AddEntityException, EntityNotFoundException {
        RunAddPayload addPayload = runMapper.toDomainAddPayload(request, eventId);
        Run domainEntity = runEntityService.add(addPayload);
        RunApiEntity run = runMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromPath("/events/{eventId}/runs/{runId}")
                .build(eventId, run.getId()))
                .build();
    }
}
