package org.coner.resource;

import io.dropwizard.hibernate.UnitOfWork;
import org.coner.api.entity.HandicapGroup;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;

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

    @GET
    @UnitOfWork
    public HandicapGroup getHandicapGroup(@PathParam("handicapGroupId") String id) {
        org.coner.core.domain.HandicapGroup domainHandicapGroup = conerCoreService.getHandicapGroup(id);
        if (domainHandicapGroup == null) {
            throw new NotFoundException("No handicap group with id " + id);
        }
        org.coner.api.entity.HandicapGroup handicapGroup = handicapGroupBoundary.toApiEntity(domainHandicapGroup);
        return handicapGroup;
    }
}
