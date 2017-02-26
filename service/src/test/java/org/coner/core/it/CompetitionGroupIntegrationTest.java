package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.response.GetCompetitionGroupSetsResponse;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import io.dropwizard.jersey.validation.ValidationErrorMessage;

public class CompetitionGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String COMPETITION_GROUPS_PATH = "/competitionGroups";
    private static final String COMPETITION_GROUP_PATH = "/competitionGroups/{competitionGroupId}";
    private static final String COMPETITION_GROUP_SETS_PATH = "/competitionGroups/sets";
    private static final String COMPETITION_GROUP_SET_PATH = "/competitionGroups/sets/{competitionGroupId}";

    @Test
    public void whenCreateCompetitionGroupItShouldPersist() {
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        AddCompetitionGroupRequest addCompetitionGroupRequest = ApiRequestTestUtils.fullAddCompetitionGroupRequest();

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
        CompetitionGroupApiEntity expected = ApiEntityTestUtils.fullCompetitionGroup();
        expected.setId(competitionGroupId);
        assertThat(getCompetitionGroupResponse);
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
        ValidationErrorMessage validationErrorMessage = addCompetitionGroupResponseContainer.readEntity(
                ValidationErrorMessage.class
        );
        assertThat(validationErrorMessage.getErrors()).isNotEmpty();
    }

    @Test
    public void whenCreateCompetitionGroupSetItShouldPersist() {
        URI competitionGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils
                .fullAddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        addCompetitionGroupSetRequest.setCompetitionGroupIds(null); // perfectly ok to create an empty one

        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));

        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String competitionGroupSetId = UnitTestUtils.getEntityIdFromResponse(addCompetitionGroupSetResponseContainer);
        assertThat(competitionGroupSetId).isNotEmpty();

        URI competitionGroupSetUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SET_PATH)
                .build(competitionGroupSetId);

        Response getCompetitionGroupSetResponseContainer = client.target(competitionGroupSetUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        CompetitionGroupSetApiEntity getCompetitionGroupResponse = getCompetitionGroupSetResponseContainer
                .readEntity(CompetitionGroupSetApiEntity.class);
        assertThat(getCompetitionGroupResponse.getId()).isEqualTo(competitionGroupSetId);
    }

    @Test
    public void whenCreateInvalidCompetitionGroupSetItShouldRejectUnprocessable() {
        URI competitionGroupsSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = new AddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName("  "); // whitespace only is not ok
        addCompetitionGroupSetRequest.setCompetitionGroupIds(null);

        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupsSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));

        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    @Test
    public void whenGetAllCompetitionGroupSetsItShouldReturnIt() {
        URI competitionGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils
                .fullAddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setCompetitionGroupIds(null);

        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));
        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);

        Response getCompetitionGroupSetsResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();
        GetCompetitionGroupSetsResponse actual = getCompetitionGroupSetsResponseContainer
                .readEntity(GetCompetitionGroupSetsResponse.class);
        assertThat(actual.getEntities()).isNotEmpty();
        assertThat(actual.getEntities().get(0).getId()).isNotEmpty();
    }

}
