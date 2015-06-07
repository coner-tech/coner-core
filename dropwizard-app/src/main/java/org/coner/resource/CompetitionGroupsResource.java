package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.CompetitionGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.CompetitionGroup;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
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

    private final CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupsResource(
            CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupApiDomainBoundary = competitionGroupApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a new Competition Group")
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
            @Valid @ApiParam(value = "Competition Group") CompetitionGroupApiEntity addCompetitionGroup
    ) {
        CompetitionGroup domainCompetitionGroup = competitionGroupApiDomainBoundary.toRemoteEntity(addCompetitionGroup);
        conerCoreService.addCompetitionGroup(domainCompetitionGroup);
        return Response.created(UriBuilder.fromResource(CompetitionGroupResource.class)
                .build(domainCompetitionGroup.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Groups", response = GetCompetitionGroupsResponse.class)
    public GetCompetitionGroupsResponse getCompetitionGroups() {
        List<CompetitionGroup> domainCompetitionGroups = conerCoreService.getCompetitionGroups();
        GetCompetitionGroupsResponse response = new GetCompetitionGroupsResponse();
        response.setCompetitionGroups(competitionGroupApiDomainBoundary.toLocalEntities(domainCompetitionGroups));
        return response;
    }
}
