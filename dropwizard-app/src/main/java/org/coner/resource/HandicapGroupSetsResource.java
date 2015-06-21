package org.coner.resource;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/handicapGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupSetsResource {

    private final ConerCoreService conerCoreService;
    private final HandicapGroupSetApiDomainBoundary apiDomainBoundary;
    private final HandicapGroupSetApiAddPayloadBoundary apiAddPayloadBoundary;

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
