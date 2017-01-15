package org.coner.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.exception.EntityNotFoundException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/handicapGroups/{handicapGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupResource {

    private final HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    @Inject
    public HandicapGroupResource(
            HandicapGroupApiDomainBoundary handicapGroupApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.handicapGroupApiDomainBoundary = handicapGroupApiDomainBoundary;
        this.conerCoreService = conerCoreService;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get a Handicap Group", response = HandicapGroupApiEntity.class)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.OK_200, response = HandicapGroupApiEntity.class, message = "OK"),
            @ApiResponse(code = HttpStatus.NOT_FOUND_404, response = ErrorsResponse.class, message = "Not found")
    })
    public HandicapGroupApiEntity getHandicapGroup(
            @PathParam("handicapGroupId") @ApiParam(value = "Handicap Group ID", required = true) String id
    ) {
        HandicapGroup domainEntity = null;
        try {
            domainEntity = conerCoreService.getHandicapGroup(id);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No handicap group with id " + id);
        }
        return handicapGroupApiDomainBoundary.toLocalEntity(domainEntity);
    }
}
