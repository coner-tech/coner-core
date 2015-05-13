package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.CompetitionGroup;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The CompetitionGroupResource exposes getting, updating, and deleting a
 * CompetitionGroup via the REST API.
 */
@Path("/competitionGroups/{competitionGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupResource {

    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the CompetitionGroupResource
     *
     * @param competitionGroupBoundary the CompetitionGroupBoundary to use for converting API and Domain
     *                                 CompetitionGroup entities
     * @param conerCoreService         the ConerCoreService
     */
    public CompetitionGroupResource(
            CompetitionGroupBoundary competitionGroupBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get a CompetitionGroup by its id.
     *
     * @param id the id of the CompetitionGroup to get
     * @return the CompetitionGroup with the id
     * @throws javax.ws.rs.NotFoundException if no CompetitionGroup is found having the id
     */
    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group", response = CompetitionGroup.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroup.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public CompetitionGroup getCompetitionGroup(
            @PathParam("competitionGroupId") @ApiParam(value = "Competition Group ID", required = true) String id
    ) {
        org.coner.core.domain.CompetitionGroup domainCompetitionGroup = conerCoreService.getCompetitionGroup(id);
        if (domainCompetitionGroup == null) {
            throw new NotFoundException("No competition group with id " + id);
        }
        CompetitionGroup competitionGroup = competitionGroupBoundary.toApiEntity(domainCompetitionGroup);
        return competitionGroup;
    }
}
