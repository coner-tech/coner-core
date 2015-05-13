package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.HandicapGroup;
import org.coner.api.response.GetHandicapGroupsResponse;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.List;

/**
 * The HandicapGroupsResource exposes getting and adding Handicap Groups via the REST API.
 */
@Path("/handicapGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupsResource {

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the HandicapGroupsResource.
     *
     * @param handicapGroupBoundary the HandicapGroupBoundary to use for converting API and
     *                              Domain Handicap Group entities
     * @param conerCoreService      the ConerCoreService
     */
    public HandicapGroupsResource(HandicapGroupBoundary handicapGroupBoundary, ConerCoreService conerCoreService) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Add a HandicapGroup.
     *
     * @param handicapGroup the handicap group to add
     * @return a response containing response code and url of the added handicap group
     */
    @POST
    @UnitOfWork
    @ApiOperation(value = "Add a Handicap Group")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.CREATED_201, message = "Created at URI in Location header"),
            @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = "Failed validation")
    })
    public Response addHandicapGroup(
            @Valid @ApiParam(value = "Handicap Group") HandicapGroup handicapGroup
    ) {
        org.coner.core.domain.HandicapGroup domainHandicapGroup = handicapGroupBoundary.toDomainEntity(handicapGroup);
        conerCoreService.addHandicapGroup(domainHandicapGroup);
        return Response.created(UriBuilder.fromResource(HandicapGroupResource.class)
                .build(domainHandicapGroup.getId()))
                .build();
    }

    /**
     * Get all handicap groups.
     *
     * @return a list of all handicap groups
     */
    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Handicap Groups", response = GetHandicapGroupsResponse.class)
    public GetHandicapGroupsResponse getHandicapGroups() {
        List<org.coner.core.domain.HandicapGroup> domainHandicapGroups = conerCoreService.getHandicapGroups();
        GetHandicapGroupsResponse response = new GetHandicapGroupsResponse();
        response.setHandicapGroups(handicapGroupBoundary.toApiEntities(domainHandicapGroups));
        return response;
    }
}
