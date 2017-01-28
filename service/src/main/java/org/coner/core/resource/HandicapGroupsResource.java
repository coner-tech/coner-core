package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.response.ErrorsResponse;
import org.coner.core.api.response.GetHandicapGroupsResponse;
import org.coner.core.boundary.HandicapGroupApiAddPayloadBoundary;
import org.coner.core.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/handicapGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupsResource {

    private final HandicapGroupEntityService handicapGroupEntityService;
    private final HandicapGroupApiDomainBoundary apiDomainBoundary;
    private final HandicapGroupApiAddPayloadBoundary apiAddPayloadBoundary;

    @Inject
    public HandicapGroupsResource(
            HandicapGroupEntityService handicapGroupEntityService,
            HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary,
            HandicapGroupApiAddPayloadBoundary handicapGroupApiAddPayloadBoundary
    ) {
        this.handicapGroupEntityService = handicapGroupEntityService;
        this.apiDomainBoundary = handicapGroupApiDomainBoundary;
        this.apiAddPayloadBoundary = handicapGroupApiAddPayloadBoundary;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a Handicap Group")
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    message = "Created at URI in Location header"
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    message = "Failed validation",
                    response = ErrorsResponse.class
            )
    })
    public Response addHandicapGroup(
            @Valid @ApiParam(value = "Handicap Group") AddHandicapGroupRequest request
    ) throws AddEntityException {
        HandicapGroupAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        HandicapGroup domainEntity = handicapGroupEntityService.add(addPayload);
        HandicapGroupApiEntity entity = apiDomainBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<HandicapGroup> domainHandicapGroups = handicapGroupEntityService.getAll();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setHandicapGroups(apiDomainBoundary.toLocalEntities(domainHandicapGroups));
        return response;
    }
}
