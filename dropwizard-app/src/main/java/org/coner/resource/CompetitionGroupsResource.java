package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.request.AddCompetitionGroupRequest;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/competitionGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupsResource {

    private final ConerCoreService conerCoreService;
    private final CompetitionGroupApiDomainBoundary apiDomainBoundary;
    private final CompetitionGroupApiAddPayloadBoundary apiAddPayloadBoundary;

    public CompetitionGroupsResource(
            ConerCoreService conerCoreService,
            CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary,
            CompetitionGroupApiAddPayloadBoundary apiAddPayloadBoundary
    ) {
        this.conerCoreService = conerCoreService;
        this.apiDomainBoundary = competitionGroupApiDomainBoundary;
        this.apiAddPayloadBoundary = apiAddPayloadBoundary;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a Competition Group")
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
    public Response addCompetitionGroup(
            @Valid @ApiParam(value = "Competition Group") AddCompetitionGroupRequest request
    ) {
        CompetitionGroupAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        CompetitionGroup domainEntity = conerCoreService.addCompetitionGroup(addPayload);
        CompetitionGroupApiEntity entity = apiDomainBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(CompetitionGroupResource.class)
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Groups", response = GetCompetitionGroupsResponse.class)
    public GetCompetitionGroupsResponse getCompetitionGroups() {
        List<CompetitionGroup> domainCompetitionGroups = conerCoreService.getCompetitionGroups();
        GetCompetitionGroupsResponse response = new GetCompetitionGroupsResponse();
        response.setCompetitionGroups(apiDomainBoundary.toLocalEntities(domainCompetitionGroups));
        return response;
    }
}
