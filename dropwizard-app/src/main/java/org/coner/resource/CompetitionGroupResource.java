package org.coner.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/competitionGroups/{competitionGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupResource {

    private final CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    @Inject
    public CompetitionGroupResource(
            CompetitionGroupApiDomainBoundary competitionGroupApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupApiDomainBoundary = competitionGroupApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group", response = CompetitionGroupApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public CompetitionGroupApiEntity getCompetitionGroup(
            @PathParam("competitionGroupId") @ApiParam(value = "Competition Group ID", required = true) String id
    ) {
        CompetitionGroup domainEntity = null;
        try {
            domainEntity = conerCoreService.getCompetitionGroup(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No competition group with id " + id);
        }
        return competitionGroupApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
