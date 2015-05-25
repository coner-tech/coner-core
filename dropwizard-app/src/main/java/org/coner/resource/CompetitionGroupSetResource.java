package org.coner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The CompetitionGroupSetResource exposes getting, updating, and deleting a
 * CompetitionGroupSet via the REST API.
 */
@Path("/competitionGroups/sets/{competitionGroupSetId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompetitionGroupSetResource {
}
