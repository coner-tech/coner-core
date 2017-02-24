package org.coner.core.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.service.CompetitionGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/competitionGroups/{competitionGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.COMPETITION_GROUPS)
public class CompetitionGroupResource {

    private final CompetitionGroupMapper competitionGroupMapper;
    private final CompetitionGroupEntityService competitionGroupEntityService;

    @Inject
    public CompetitionGroupResource(
            CompetitionGroupMapper competitionGroupMapper,
            CompetitionGroupEntityService competitionGroupEntityService
    ) {
        this.competitionGroupMapper = competitionGroupMapper;
        this.competitionGroupEntityService = competitionGroupEntityService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Competition Group", response = CompetitionGroupApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = CompetitionGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorMessage.class, message = "Not found")
    })
    public CompetitionGroupApiEntity getCompetitionGroup(
            @PathParam("competitionGroupId") @ApiParam(value = "Competition Group ID", required = true) String id
    ) throws EntityNotFoundException {
        CompetitionGroup domainEntity = competitionGroupEntityService.getById(id);
        return competitionGroupMapper.toApiEntity(domainEntity);
    }
}
