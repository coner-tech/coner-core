package org.coner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The CompetitionGroupResource exposes getting, updating, and deleting a
 * CompetitionGroup via the REST API.
 */
@Path("/competitionGroups/{competitionGroupId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompetitionGroupResource {
}
