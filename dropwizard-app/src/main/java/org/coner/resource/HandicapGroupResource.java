package org.coner.resource;

import org.coner.api.entity.HandicapGroup;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.jetty.http.HttpStatus;

@Path("/handicapGroups/{handicapGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupResource {

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final ConerCoreService conerCoreService;

    public HandicapGroupResource(HandicapGroupBoundary handicapGroupBoundary, ConerCoreService conerCoreService) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Handicap Group", response = HandicapGroup.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = HandicapGroup.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public HandicapGroup getHandicapGroup(
            @PathParam("handicapGroupId") @ApiParam(value = "Handicap Group ID", required = true) String id
    ) {
        org.coner.core.domain.HandicapGroup domainHandicapGroup = conerCoreService.getHandicapGroup(id);
        if (domainHandicapGroup == null) {
            throw new NotFoundException("No handicap group with id " + id);
        }
        org.coner.api.entity.HandicapGroup handicapGroup = handicapGroupBoundary.toApiEntity(domainHandicapGroup);
        return handicapGroup;
    }
}
