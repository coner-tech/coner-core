package org.coner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/competitionGroups/sets/{competitionGroupSetId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupSetResource {

    private final CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupSetResource(
            ConerCoreService conerCoreService, CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary
    ) {
        this.competitionGroupSetApiDomainBoundary = competitionGroupSetApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group Set", response = CompetitionGroupSetApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public CompetitionGroupSetApiEntity getCompetitionGroupSet(
            @PathParam("competitionGroupSetId")
            @ApiParam(value = "Competition Group Set ID", required = true) String id
    ) {
        CompetitionGroupSet domainEntity = null;
        try {
            domainEntity = conerCoreService.getCompetitionGroupSet(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No competition group set found with id " + id);
        }
        return competitionGroupSetApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
