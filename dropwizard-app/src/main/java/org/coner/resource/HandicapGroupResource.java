package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.HandicapGroup;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The HandicapGroupResource exposes getting, updating, and deleting a
 * HandicapGroup via the REST API.
 */
@Path("/handicapGroups/{handicapGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupResource {

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the HandicapGroupResource
     *
     * @param handicapGroupBoundary the HandicapGroupBoundary to use for converting API and Domain HandicapGroup
     *                              entities
     * @param conerCoreService      the ConerCoreService
     */
    public HandicapGroupResource(HandicapGroupBoundary handicapGroupBoundary, ConerCoreService conerCoreService) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Get a HandicapGroup by its id.
     *
     * @param id the id of the HandicapGroup to get
     * @return the HandicapGroup with the id
     * @throws javax.ws.rs.NotFoundException if no HandicapGroup is found having the id
     */
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
