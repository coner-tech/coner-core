package org.coner.resource;

import org.coner.api.entity.HandicapGroup;
import org.coner.api.response.*;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
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

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final ConerCoreService conerCoreService;

    public HandicapGroupsResource(HandicapGroupBoundary handicapGroupBoundary, ConerCoreService conerCoreService) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.conerCoreService = conerCoreService;
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
            @Valid @ApiParam(value = "Handicap Group") HandicapGroup handicapGroup
    ) {
        org.coner.core.domain.HandicapGroup domainHandicapGroup = handicapGroupBoundary.toDomainEntity(handicapGroup);
        conerCoreService.addHandicapGroup(domainHandicapGroup);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(domainHandicapGroup.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<org.coner.core.domain.HandicapGroup> domainHandicapGroups = conerCoreService.getHandicapGroups();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setHandicapGroups(handicapGroupBoundary.toApiEntities(domainHandicapGroups));
        return response;
    }
}
