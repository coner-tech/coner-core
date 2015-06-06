package org.coner.resource;

import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.*;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.*;
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

    private final HandicapGroupSetBoundary handicapGroupSetBoundary;
    private final ConerCoreService conerCoreService;

    public HandicapGroupSetsResource(
            HandicapGroupSetBoundary handicapGroupSetBoundary,
            ConerCoreService conerCoreService
    ) {
        this.handicapGroupSetBoundary = handicapGroupSetBoundary;
        this.conerCoreService = conerCoreService;
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
        HandicapGroupSet domainHandicapGroupSet = handicapGroupSetBoundary.toDomainEntity(request);
        Set<AddHandicapGroupSetRequest.HandicapGroup> apiHandicapGroups = request.getHandicapGroups();
        if (apiHandicapGroups != null) {
            Set<HandicapGroup> domainHandicapGroups = new HashSet<>();
            for (AddHandicapGroupSetRequest.HandicapGroup apiHandicapGroup : apiHandicapGroups) {
                HandicapGroup domainHandicapGroup = conerCoreService.getHandicapGroup(
                        apiHandicapGroup.getId()
                );
                if (domainHandicapGroup == null) {
                    throw new NotFoundException("No handicap group with id " + apiHandicapGroup.getId());
                }
                domainHandicapGroups.add(domainHandicapGroup);
            }
            domainHandicapGroupSet.setHandicapGroups(domainHandicapGroups);
        }
        conerCoreService.addHandicapGroupSet(domainHandicapGroupSet);
        return Response.created(UriBuilder.fromResource(HandicapGroupSetResource.class)
                .build(domainHandicapGroupSet.getId()))
                .build();
    }
}
