package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
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
    private final CompetitionGroupSetService competitionGroupSetService;

    @Inject
    public CompetitionGroupSetResource(
            CompetitionGroupSetService competitionGroupSetService,
            CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary
    ) {
        this.competitionGroupSetApiDomainBoundary = competitionGroupSetApiDomainBoundary;
        this.competitionGroupSetService = competitionGroupSetService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group Set", response = CompetitionGroupSetApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorMessage.class, message = "Not found")
    })
    public CompetitionGroupSetApiEntity getCompetitionGroupSet(
            @PathParam("competitionGroupSetId")
            @ApiParam(value = "Competition Group Set ID", required = true) String id
    ) throws EntityNotFoundException {
        CompetitionGroupSet domainEntity = competitionGroupSetService.getById(id);
        return competitionGroupSetApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
