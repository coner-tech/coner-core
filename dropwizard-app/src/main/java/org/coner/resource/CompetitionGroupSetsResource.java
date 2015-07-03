package org.coner.resource;

import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.api.response.*;
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.*;

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

    private final CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupSetsResource(
            CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary,
            ConerCoreService conerCoreService
    ) {
        this.competitionGroupSetApiDomainBoundary = competitionGroupSetApiDomainBoundary;
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
        CompetitionGroupSet domainCompetitionGroupSet = competitionGroupSetApiDomainBoundary
                .toRemoteEntity(request);
        Set<AddCompetitionGroupSetRequest.CompetitionGroup> requestCompetitionGroups = request.getCompetitionGroups();
        if (requestCompetitionGroups != null) {
            Set<CompetitionGroup> domainCompetitionGroups = new HashSet<>();
            for (AddCompetitionGroupSetRequest.CompetitionGroup requestCompetitionGroup : requestCompetitionGroups) {
                CompetitionGroup domainCompetitionGroup = conerCoreService.getCompetitionGroup(
                        requestCompetitionGroup.getId()
                );
                if (domainCompetitionGroup == null) {
                    throw new NotFoundException("No competition group with id " + requestCompetitionGroup.getId());
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

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Group Sets", response = GetCompetitionGroupSetsResponse.class)
    public GetCompetitionGroupSetsResponse getCompetitionGroupSets() {
        List<CompetitionGroupSet> domainCompetitionGroupSets = conerCoreService.getCompetitionGroupSets();
        GetCompetitionGroupSetsResponse response = new GetCompetitionGroupSetsResponse();
        response.setCompetitionGroupSets(competitionGroupSetApiDomainBoundary
        .toLocalEntities(domainCompetitionGroupSets));
        return response;
    }
}
