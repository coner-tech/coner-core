package org.coner.resource;

import org.coner.api.entity.*;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.exception.EntityNotFoundException;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.jetty.http.HttpStatus;

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
