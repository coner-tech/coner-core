package org.coner.it;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.request.*;
import org.coner.api.response.ErrorsResponse;
import org.coner.util.*;

import java.net.URI;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompetitionGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String COMPETITION_GROUPS_PATH = "/competitionGroups";
    private static final String COMPETITION_GROUP_PATH = "/competitionGroups/{competitionGroupId}";
    private static final String COMPETITION_GROUP_SETS_PATH = "/competitionGroups/sets";

    @Test
    public void whenCreateCompetitionGroupItShouldPersist() {
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        AddCompetitionGroupRequest addCompetitionGroupRequest = new AddCompetitionGroupRequest();
        addCompetitionGroupRequest.setName(TestConstants.COMPETITION_GROUP_NAME);
        addCompetitionGroupRequest.setHandicapFactor(TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR);
        addCompetitionGroupRequest.setResultTimeType(TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name());
        addCompetitionGroupRequest.setGrouping(TestConstants.COMPETITION_GROUP_GROUPING);

        Response addCompetitionGroupResponseContainer = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupRequest));

        assertThat(addCompetitionGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String competitionGroupId = UnitTestUtils.getEntityIdFromResponse(addCompetitionGroupResponseContainer);
        assertThat(competitionGroupId).isNotNull();

        URI competitionGroupUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_PATH)
                .build(competitionGroupId);

        Response getCompetitionGroupResponseContainer = client.target(competitionGroupUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getCompetitionGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        CompetitionGroupApiEntity getCompetitionGroupResponse = getCompetitionGroupResponseContainer
                .readEntity(CompetitionGroupApiEntity.class);
        assertThat(getCompetitionGroupResponse.getId()).isEqualTo(competitionGroupId);
    }

    @Test
    public void whenCreateInvalidCompetitionGroupItShouldReject() {
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        AddCompetitionGroupRequest addCompetitionGroupRequest = new AddCompetitionGroupRequest();

        Response addCompetitionGroupResponseContainer = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupRequest));

        assertThat(addCompetitionGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = addCompetitionGroupResponseContainer.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
    }

    @Test
    public void whenCreateCompetitionGroupSetItShouldPersist() {
        URI competitionGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = new AddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        addCompetitionGroupSetRequest.setCompetitionGroups(null); // perfectly ok to create an empty one

        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));

        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String competitionGroupSetId = UnitTestUtils.getEntityIdFromResponse(addCompetitionGroupSetResponseContainer);
        assertThat(competitionGroupSetId).isNotEmpty();
    }

    @Test
    public void whenCreateInvalidCompetitionGroupSetItShouldRejectUnprocessable() {
        URI competitionGroupsSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = new AddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName("  "); // whitespace only is not ok
        addCompetitionGroupSetRequest.setCompetitionGroups(null);

        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupsSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));

        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

}
