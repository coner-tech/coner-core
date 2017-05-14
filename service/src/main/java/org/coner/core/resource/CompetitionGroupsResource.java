package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.response.GetCompetitionGroupsResponse;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.service.CompetitionGroupEntityService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.util.swagger.ApiResponseConstants;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/competitionGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.COMPETITION_GROUPS)
public class CompetitionGroupsResource {

    private final CompetitionGroupEntityService competitionGroupEntityService;
    private final CompetitionGroupMapper competitionGroupMapper;

    @Inject
    public CompetitionGroupsResource(
            CompetitionGroupEntityService competitionGroupEntityService,
            CompetitionGroupMapper competitionGroupMapper
    ) {
        this.competitionGroupEntityService = competitionGroupEntityService;
        this.competitionGroupMapper = competitionGroupMapper;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a Competition Group")
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    message = ApiResponseConstants.Created.MESSAGE,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = ApiResponseConstants.Created.Headers.NAME,
                                    description = ApiResponseConstants.Created.Headers.DESCRIPTION,
                                    response = String.class
                            )
                    }
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    message = "Failed validation",
                    response = ValidationErrorMessage.class
            )
    })
    public Response addCompetitionGroup(
            @Valid @ApiParam(value = "Competition Group") AddCompetitionGroupRequest request
    ) throws AddEntityException {
        CompetitionGroupAddPayload addPayload = competitionGroupMapper.toDomainAddPayload(request);
        CompetitionGroup domainEntity = competitionGroupEntityService.add(addPayload);
        CompetitionGroupApiEntity entity = competitionGroupMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromPath("/competitionGroups/{competitionGroupId}")
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Groups", response = GetCompetitionGroupsResponse.class)
    public GetCompetitionGroupsResponse getCompetitionGroups() {
        List<CompetitionGroup> domainCompetitionGroups = competitionGroupEntityService.getAll();
        GetCompetitionGroupsResponse response = new GetCompetitionGroupsResponse();
        response.setEntities(competitionGroupMapper.toApiEntityList(domainCompetitionGroups));
        return response;
    }


    @GET
    @Path("/{competitionGroupId}")
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
