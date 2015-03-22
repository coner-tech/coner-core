package org.coner.it;

import org.coner.api.entity.CompetitionGroup;
import org.coner.api.request.AddCompetitionGroupRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.util.TestConstants;
import org.coner.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class CompetitionGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String COMPETITION_GROUPS_PATH = "/competitionGroups";
    private static final String COMPETITION_GROUP_PATH = "/competitionGroups/{competitionGroupId}";

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
        CompetitionGroup getCompetitionGroupResponse = getCompetitionGroupResponseContainer
                .readEntity(CompetitionGroup.class);
        assertThat(getCompetitionGroupResponse.getId()).isEqualTo(competitionGroupId);
    }

    @Test
    public void whenCreateInvalidHandicapGroupItShouldReject() {
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

}
