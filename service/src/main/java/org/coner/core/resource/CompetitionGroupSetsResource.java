package org.coner.core.resource;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.response.ErrorsResponse;
import org.coner.core.api.response.GetCompetitionGroupSetsResponse;
import org.coner.core.boundary.CompetitionGroupSetApiAddPayloadBoundary;
import org.coner.core.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.exception.AddEntityException;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/competitionGroups/sets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupSetsResource {

    private final CompetitionGroupSetService competitionGroupSetService;
    private final CompetitionGroupSetApiDomainBoundary apiDomainBoundary;
    private final CompetitionGroupSetApiAddPayloadBoundary apiAddPayloadBoundary;

    @Inject
    public CompetitionGroupSetsResource(
            CompetitionGroupSetService competitionGroupSetService,
            CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary,
            CompetitionGroupSetApiAddPayloadBoundary competitionGroupSetApiAddPayloadBoundary
    ) {
        this.competitionGroupSetService = competitionGroupSetService;
        this.apiDomainBoundary = competitionGroupSetApiDomainBoundary;
        this.apiAddPayloadBoundary = competitionGroupSetApiAddPayloadBoundary;
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
        CompetitionGroupSetAddPayload addPayload = apiAddPayloadBoundary.toRemoteEntity(request);
        CompetitionGroupSetApiEntity entity;
        try {
            CompetitionGroupSet domainEntity = competitionGroupSetService.add(addPayload);
            entity = apiDomainBoundary.toLocalEntity(domainEntity);
        } catch (AddEntityException e) {
            return Response.status(HttpStatus.UNPROCESSABLE_ENTITY_422)
                    .entity(new ErrorsResponse(e.getMessage()))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
        return Response.created(UriBuilder.fromResource(CompetitionGroupSetResource.class)
                .build(entity.getId()))
                .build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Get all Competition Group Sets", response = GetCompetitionGroupSetsResponse.class)
    public GetCompetitionGroupSetsResponse getCompetitionGroupSets() {
        List<CompetitionGroupSet> domainCompetitionGroupSets = competitionGroupSetService.getAll();
        GetCompetitionGroupSetsResponse response = new GetCompetitionGroupSetsResponse();
        response.setCompetitionGroupSets(apiDomainBoundary.toLocalEntities(domainCompetitionGroupSets));
        return response;
    }
}
