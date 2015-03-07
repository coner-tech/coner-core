package org.coner.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
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
}
