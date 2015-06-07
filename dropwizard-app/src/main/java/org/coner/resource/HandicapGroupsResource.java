package org.coner.resource;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.HandicapGroup;

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

    private final HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public HandicapGroupsResource(
            HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.handicapGroupApiDomainBoundary = handicapGroupApiDomainBoundary;
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
            @Valid @ApiParam(value = "Handicap Group") HandicapGroupApiEntity handicapGroup
    ) {
        HandicapGroup domainHandicapGroup = handicapGroupApiDomainBoundary.toRemoteEntity(handicapGroup);
        conerCoreService.addHandicapGroup(domainHandicapGroup);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(domainHandicapGroup.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<HandicapGroup> domainHandicapGroups = conerCoreService.getHandicapGroups();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setHandicapGroups(handicapGroupApiDomainBoundary.toLocalEntities(domainHandicapGroups));
        return response;
    }
}
