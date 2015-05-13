package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.CompetitionGroup;
import org.coner.api.response.GetCompetitionGroupsResponse;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * The CompetitionGroupsResource exposes getting and adding CompetitionGroups
 * via the REST API.
 */
@Path("/competitionGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupsResource {

    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the CompetitionGroupsResource.
     *
     * @param competitionGroupBoundary the HandicapGroupBoundary to use for converting API and Domain Handicap Group
     *                                 entities
     * @param conerCoreService         the ConerCoreService
     */
    public CompetitionGroupsResource(
            CompetitionGroupBoundary competitionGroupBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Add a competition group.
     *
     * @param competitionGroup the competitionGroup to add
     * @return a response containing the added Event
     */
    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a new Competition Group")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.CREATED_201, message = "Created at URI in Location header"),
            @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = "Failed validation")
    })
    public Response addCompetitionGroup(
            @Valid @ApiParam(value = "Competition Group") CompetitionGroup competitionGroup
    ) {
        org.coner.core.domain.CompetitionGroup domainCompetitionGroup = competitionGroupBoundary.toDomainEntity(
                competitionGroup
        );
        conerCoreService.addCompetitionGroup(domainCompetitionGroup);
        return Response.created(UriBuilder.fromResource(CompetitionGroupResource.class)
                .build(domainCompetitionGroup.getId()))
                .build();
    }

    /**
     * Get all competition groups.
     *
     * @return a list of all competition groups
     */
    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Groups", response = GetCompetitionGroupsResponse.class)
    public GetCompetitionGroupsResponse getCompetitionGroups() {
        List<org.coner.core.domain.CompetitionGroup> domainCompetitionGroups = conerCoreService.getCompetitionGroups();
        GetCompetitionGroupsResponse response = new GetCompetitionGroupsResponse();
        response.setCompetitionGroups(competitionGroupBoundary.toApiEntities(domainCompetitionGroups));
        return response;
    }
}
