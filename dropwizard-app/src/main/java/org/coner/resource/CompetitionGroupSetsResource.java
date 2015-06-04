package org.coner.resource;

import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupSetBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.CompetitionGroup;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import java.util.*;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;

@Path("/competitionGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupSetsResource {

    private final CompetitionGroupSetBoundary competitionGroupSetBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupSetsResource(
            CompetitionGroupSetBoundary competitionGroupSetBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupSetBoundary = competitionGroupSetBoundary;
        this.conerCoreService = conerCoreService;
    }

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
        org.coner.core.domain.CompetitionGroupSet domainCompetitionGroupSet = competitionGroupSetBoundary
                .toDomainEntity(request);
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
