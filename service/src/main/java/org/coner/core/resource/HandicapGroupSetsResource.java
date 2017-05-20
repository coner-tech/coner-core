package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.response.GetHandicapGroupSetsResponse;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.HandicapGroupSetMapper;
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

@Path("/handicapGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.HANDICAP_GROUPS)
public class HandicapGroupSetsResource {

    private final HandicapGroupSetService handicapGroupSetService;
    private final HandicapGroupSetMapper handicapGroupSetMapper;

    @Inject
    public HandicapGroupSetsResource(
            HandicapGroupSetService handicapGroupSetService,
            HandicapGroupSetMapper handicapGroupSetMapper
    ) {
        this.handicapGroupSetService = handicapGroupSetService;
        this.handicapGroupSetMapper = handicapGroupSetMapper;
    }

    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Add a new Handicap Group Set",
            notes = "Optionally include a set of Handicap Group entities with ID to associate them"
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
            @Valid @NotNull @ApiParam(value = "Handicap Group Set") AddHandicapGroupSetRequest request
    ) throws AddEntityException {
        HandicapGroupSetAddPayload addPayload = handicapGroupSetMapper.toDomainAddPayload(request);
        HandicapGroupSet domainEntity = handicapGroupSetService.add(addPayload);
        HandicapGroupSetApiEntity entity = handicapGroupSetMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromPath("/handicapGroups/sets/{handicapGroupSetId}")
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Group Sets", response = GetHandicapGroupSetsResponse.class)
    public GetHandicapGroupSetsResponse getHandicapGroupSets() {
        List<HandicapGroupSet> domainHandicapGroupSets = handicapGroupSetService.getAll();
        GetHandicapGroupSetsResponse response = new GetHandicapGroupSetsResponse();
        response.setEntities(handicapGroupSetMapper.toApiEntityList(domainHandicapGroupSets));
        return response;
    }

    @GET
    @Path("/{handicapGroupSetId}")
    @UnitOfWork
    @ApiOperation(value = "Get a Handicap Group Set", response = HandicapGroupSetApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = HandicapGroupSetApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorMessage.class, message = "Not found")
    })
    public HandicapGroupSetApiEntity getHandicapGroupSet(
            @PathParam("handicapGroupSetId")
            @ApiParam(value = "Handicap Group Set ID", required = true) String id
    ) throws EntityNotFoundException {
        HandicapGroupSet domainEntity = handicapGroupSetService.getById(id);
        return handicapGroupSetMapper.toApiEntity(domainEntity);
    }
}
