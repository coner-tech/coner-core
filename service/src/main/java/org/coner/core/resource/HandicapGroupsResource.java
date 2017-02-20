package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.response.GetHandicapGroupsResponse;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.coner.core.mapper.HandicapGroupMapper;
import org.coner.core.util.swagger.ApiResponseConstants;
import org.coner.core.util.swagger.ApiTagConstants;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/handicapGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(tags = ApiTagConstants.HANDICAP_GROUPS)
public class HandicapGroupsResource {

    private final HandicapGroupEntityService handicapGroupEntityService;
    private final HandicapGroupMapper handicapGroupMapper;

    @Inject
    public HandicapGroupsResource(
            HandicapGroupEntityService handicapGroupEntityService,
            HandicapGroupMapper handicapGroupMapper
    ) {
        this.handicapGroupEntityService = handicapGroupEntityService;
        this.handicapGroupMapper = handicapGroupMapper;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a Handicap Group")
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
    public Response addHandicapGroup(
            @Valid @ApiParam(value = "Handicap Group") AddHandicapGroupRequest request
    ) throws AddEntityException {
        HandicapGroupAddPayload addPayload = handicapGroupMapper.toDomainAddPayload(request);
        HandicapGroup domainEntity = handicapGroupEntityService.add(addPayload);
        HandicapGroupApiEntity entity = handicapGroupMapper.toApiEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<HandicapGroup> domainHandicapGroups = handicapGroupEntityService.getAll();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setEntities(handicapGroupMapper.toApiEntityList(domainHandicapGroups));
        return response;
    }
}
