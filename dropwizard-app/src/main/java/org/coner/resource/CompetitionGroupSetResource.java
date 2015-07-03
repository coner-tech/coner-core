package org.coner.resource;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
//import org.coner.api.response.ErrorsResponse; // Not in use yet
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroupSet;

import com.wordnik.swagger.annotations.*;
import io.dropwizard.hibernate.UnitOfWork;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//import org.eclipse.jetty.http.HttpStatus; // Not in use yet

@Path("/competitionGroups/sets/{competitionGroupSetId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Competition Groups")
public class CompetitionGroupSetResource {

    private final CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary;
    private final ConerCoreService conerCoreService;

    public CompetitionGroupSetResource(
            CompetitionGroupSetApiDomainBoundary competitionGroupSetApiDomainBoundary,
            ConerCoreService conerCoreService
        ) {
            this.competitionGroupSetApiDomainBoundary = competitionGroupSetApiDomainBoundary;
            this.conerCoreService = conerCoreService;
        }

        @GET
        @UnitOfWork
        @ApiOperation(value = "Get a single Competition Group Set", response = CompetitionGroupSetApiEntity.class)

        public CompetitionGroupSetApiEntity getCompetitionGroupSet(
                @PathParam("competitionGroupSetId")
                @ApiParam(value = "Competition Group Set ID",
                required = true) String id
            ) {
            CompetitionGroupSet domainCompetitionGroupSet = conerCoreService.getCompetitionGroupSet(id);
            if (domainCompetitionGroupSet == null) {
                throw new NotFoundException("No competition group set with id " + id);
            }

            return competitionGroupSetApiDomainBoundary.toLocalEntity(domainCompetitionGroupSet);
        }

}
