package org.coner.resource;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ResponseHeader;
import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.boundary.CompetitionGroupSetBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.CompetitionGroup;
import org.coner.core.domain.CompetitionGroupSet;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
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
 * The CompetitionGroupSetsResource exposes getting and adding CompetitionGroup Sets
 * via the REST API.
 */
@Path("/competitionGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupSetsResource {

    private final CompetitionGroupSetBoundary competitionGroupSetBoundary;
    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final ConerCoreService conerCoreService;

    /**
     * Constructor for the CompetitionGroupsSetsResource.
     *
     * @param competitionGroupSetBoundary the CompetitionGroupSetBoundary to use for converting API and Domain
     *                                    Competition Group Set entities
     * @param competitionGroupBoundary    the CompetitionGroupBoundary to use for converting API and Domain Competition
     *                                    Group entities
     * @param conerCoreService            the ConerCoreService
     */
    public CompetitionGroupSetsResource(
            CompetitionGroupSetBoundary competitionGroupSetBoundary,
            CompetitionGroupBoundary competitionGroupBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupSetBoundary = competitionGroupSetBoundary;
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.conerCoreService = conerCoreService;
    }

    /**
     * Add a competition group set.
     *
     * @param request the CompetitionGroupSet to add
     * @return the response
     */
    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Add a new Competition Group Set",
            notes = "Optionally include a list of Competition Group entities with ID to associate them"
    )
    @ApiResponses({
            @ApiResponse(
                    code = HttpStatus.CREATED_201,
                    message = "Created",
                    responseHeaders = {
                            @ResponseHeader(name = "Location", description = "URI of created Competition Group Set")
                    }
            ),
            @ApiResponse(
                    code = HttpStatus.UNPROCESSABLE_ENTITY_422,
                    message = "Failed validation",
                    response = ErrorsResponse.class
            )
    })
    public Response add(
            @Valid @ApiParam(value = "Competition Group Set") AddCompetitionGroupSetRequest request

    ) {
        org.coner.core.domain.CompetitionGroupSet domainCompetitionGroupSet = new CompetitionGroupSet();
        domainCompetitionGroupSet.setName(request.getName());
        Set<AddCompetitionGroupSetRequest.CompetitionGroup> rxApiCompetitionGroups = request.getCompetitionGroups();
        if (rxApiCompetitionGroups != null) {
            Set<org.coner.core.domain.CompetitionGroup> domainCompetitionGroups = new HashSet<>();
            for (AddCompetitionGroupSetRequest.CompetitionGroup rxCompetitionGroup : rxApiCompetitionGroups) {
                CompetitionGroup domainCompetitionGroup = conerCoreService.getCompetitionGroup(
                        rxCompetitionGroup.getId()
                );
                if (domainCompetitionGroup == null) {
                    throw new NotFoundException("No competition group with id " + rxCompetitionGroup.getId());
                }
                domainCompetitionGroups.add(domainCompetitionGroup);
            }
            domainCompetitionGroupSet.setCompetitionGroups(domainCompetitionGroups);
        }
        conerCoreService.addCompetitionGroupSet(domainCompetitionGroupSet);
        return Response.created(UriBuilder.fromResource(CompetitionGroupSetResource.class)
                .build(domainCompetitionGroupSet.getId()))
                .build();
    }
}
