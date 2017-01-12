package org.coner.resource;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.api.request.AddHandicapGroupRequest;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/handicapGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupsResource {

    private final ConerCoreService conerCoreService;
    private final HandicapGroupApiDomainBoundary apiDomainBoundary;
    private final HandicapGroupApiAddPayloadBoundary apiAddPayloadBoundary;

    public HandicapGroupsResource(
            ConerCoreService conerCoreService,
            HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary,
            HandicapGroupApiAddPayloadBoundary handicapGroupApiAddPayloadBoundary
    ) {
        this.conerCoreService = conerCoreService;
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
    ) {
        HandicapGroupAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        HandicapGroup domainEntity = conerCoreService.addHandicapGroup(addPayload);
        HandicapGroupApiEntity entity = apiDomainBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<HandicapGroup> domainHandicapGroups = conerCoreService.getHandicapGroups();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setHandicapGroups(apiDomainBoundary.toLocalEntities(domainHandicapGroups));
        return response;
    }
}
