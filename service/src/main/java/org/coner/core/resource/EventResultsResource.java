package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.core.api.response.GetEventResultsRegistrationResponse;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.GetRegistrationResultsPayload;
import org.coner.core.domain.service.EventRegistrationService;
import org.coner.core.domain.service.ResultsService;
import org.coner.core.domain.service.exception.EntityMismatchException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.ResultsMapper;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/events/{eventId}/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = {ApiTagConstants.EVENTS, ApiTagConstants.RESULTS})
public class EventResultsResource {

    private final EventRegistrationService eventRegistrationService;
    private final ResultsService resultsService;
    private final ResultsMapper resultsMapper;


    @Inject
    public EventResultsResource(
            EventRegistrationService eventRegistrationService,
            ResultsService resultsService,
            ResultsMapper resultsMapper
    ) {
        this.eventRegistrationService = eventRegistrationService;
        this.resultsService = resultsService;
        this.resultsMapper = resultsMapper;
    }

    @GET
    @Path("/registration/{registrationId}")
    @UnitOfWork
    @ApiOperation(
            value = "Get results for a registration at an event",
            response = GetEventResultsRegistrationResponse.class
    )
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.OK_200,
                    message = "Success",
                    response = GetEventResultsRegistrationResponse.class
            )
    })
    public GetEventResultsRegistrationResponse getEventRegistrationResults(
            @PathParam("eventId") @ApiParam(value = "Event ID", required = true) String eventId,
            @PathParam("registrationId") @ApiParam(value = "Registration ID", required = true) String registrationId
    ) throws EntityNotFoundException, EntityMismatchException {
        Registration registration = eventRegistrationService.getByEventIdAndRegistrationId(eventId, registrationId);
        GetRegistrationResultsPayload payload = resultsService.getResultsFor(registration);
        GetEventResultsRegistrationResponse response = resultsMapper.toApiResponse(payload);
        return response;
    }
}
