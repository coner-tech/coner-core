package org.coner.resource;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupSetApiAddPayloadBoundary;
import org.coner.boundary.HandicapGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/handicapGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupSetsResource {

    private final ConerCoreService conerCoreService;
    private final HandicapGroupSetApiDomainBoundary apiDomainBoundary;
    private final HandicapGroupSetApiAddPayloadBoundary apiAddPayloadBoundary;

    @Inject
    public HandicapGroupSetsResource(
            ConerCoreService conerCoreService,
            HandicapGroupSetApiDomainBoundary handicapGroupSetBoundary,
            HandicapGroupSetApiAddPayloadBoundary handicapGroupSetApiAddPayloadBoundary
    ) {
        this.conerCoreService = conerCoreService;
        this.apiDomainBoundary = handicapGroupSetBoundary;
        this.apiAddPayloadBoundary = handicapGroupSetApiAddPayloadBoundary;
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
                    message = "Created",
                    responseHeaders = {
                            @ResponseHeader(name = "Location", description = "URI of created Handicap Group Set")
                    }
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    message = "Failed validation",
                    response = ErrorsResponse.class
            )
    })
    public Response add(
            @Valid @NotNull @ApiParam(value = "Handicap Group Set") AddHandicapGroupSetRequest request
    ) {
        HandicapGroupSetAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        HandicapGroupSet domainEntity = conerCoreService.addHandicapGroupSet(addPayload);
        HandicapGroupSetApiEntity entity = apiDomainBoundary.toLocalEntity(domainEntity);
        return Response.created(UriBuilder.fromResource(HandicapGroupSetResource.class)
                .build(entity.getId()))
                .build();
    }
}
