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
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.response.GetCompetitionGroupSetsResponse;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.CompetitionGroupSetMapper;
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

@Path("/competitionGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.COMPETITION_GROUPS)
public class CompetitionGroupSetsResource {

    private final CompetitionGroupSetService competitionGroupSetService;
    private final CompetitionGroupSetMapper competitionGroupSetMapper;

    @Inject
    public CompetitionGroupSetsResource(
            CompetitionGroupSetService competitionGroupSetService,
            CompetitionGroupSetMapper competitionGroupSetMapper
    ) {
        this.competitionGroupSetService = competitionGroupSetService;
        this.competitionGroupSetMapper = competitionGroupSetMapper;
    }

    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Add a new Competition Group Set",
            notes = "Optionally include a list of Competition Group entities with ID to associate them"
    )
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
    public Response add(
            @Valid @ApiParam(value = "Competition Group Set") AddCompetitionGroupSetRequest request
    ) throws AddEntityException {
        CompetitionGroupSetAddPayload addPayload = competitionGroupSetMapper.toDomainAddPayload(request);
        CompetitionGroupSet domainEntity = competitionGroupSetService.add(addPayload);
        CompetitionGroupSetApiEntity entity = competitionGroupSetMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromPath("/competitionGroups/sets/{competitionGroupSetId}")
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Group Sets", response = GetCompetitionGroupSetsResponse.class)
    public GetCompetitionGroupSetsResponse getCompetitionGroupSets() {
        List<CompetitionGroupSet> domainCompetitionGroupSets = competitionGroupSetService.getAll();
        GetCompetitionGroupSetsResponse response = new GetCompetitionGroupSetsResponse();
        response.setEntities(competitionGroupSetMapper.toApiEntityList(domainCompetitionGroupSets));
        return response;
    }


    @GET
    @Path("/{competitionGroupSetId}")
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
        return competitionGroupSetMapper.toApiEntity(domainEntity);
    }
}
