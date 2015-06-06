package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ResponseHeader;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.ConerCoreService;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.HashSet;
import java.util.Set;

/**
 * The HandicapGroupSetsResource exposes getting and adding HandicapGroupSets via the REST API.
 */
@Path("/handicapGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Handicap Groups")
public class HandicapGroupSetsResource {

    private final HandicapGroupSetBoundary handicapGroupSetBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the HandicapGroupSetsResource
     *
     * @param handicapGroupSetBoundary the HandicapGroupSetBoundary to use for converting API to Domain entities
     * @param conerCoreService         the ConerCoreService
     */
    public HandicapGroupSetsResource(
            HandicapGroupSetBoundary handicapGroupSetBoundary,
            ConerCoreService conerCoreService
    ) {
        this.handicapGroupSetBoundary = handicapGroupSetBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Add a handicap group set.
     *
     * @param request the HandicapGroupSet to add
     * @return the response
     */
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
        org.coner.core.domain.HandicapGroupSet domainHandicapGroupSet = handicapGroupSetBoundary
                .toDomainEntity(request);
        Set<AddHandicapGroupSetRequest.HandicapGroup> apiHandicapGroups = request.getHandicapGroups();
        if (apiHandicapGroups != null) {
            Set<org.coner.core.domain.HandicapGroup> domainHandicapGroups = new HashSet<>();
            for (AddHandicapGroupSetRequest.HandicapGroup apiHandicapGroup : apiHandicapGroups) {
                org.coner.core.domain.HandicapGroup domainHandicapGroup = conerCoreService.getHandicapGroup(
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
