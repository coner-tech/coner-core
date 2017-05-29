package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.response.GetCompetitionGroupSetsResponse;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.jersey.validation.ValidationErrorMessage;

public class CompetitionGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String COMPETITION_GROUPS_PATH = "/competitionGroups";
    private static final String COMPETITION_GROUP_PATH = "/competitionGroups/{competitionGroupId}";
    private static final String COMPETITION_GROUP_SETS_PATH = "/competitionGroups/sets";
    private static final String COMPETITION_GROUP_SET_PATH = "/competitionGroups/sets/{competitionGroupSetId}";
    private static final String COMPETITION_GROUP_SET_ADD_COMPETITION_GROUP_PATH =
            "/competitionGroups/sets/{competitionGroupSetId}/competitionGroups/{competitionGroupId}";

    @Test
    public void whenCreateCompetitionGroupItShouldPersist() {
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        AddCompetitionGroupRequest addCompetitionGroupRequest = ApiRequestTestUtils.fullAddCompetitionGroup();

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
        AddCompetitionGroupRequest addCompetitionGroupRequest = ApiRequestTestUtils.fullAddCompetitionGroup();
        addCompetitionGroupRequest.setName(null);
        addCompetitionGroupRequest.setResultTimeType(null);

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
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet();
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
    public void whenCreateCompetitionGroupSetWithMultipleIdsItShouldPersist() {
        // add some initial competition group IDs
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        Response addOpenResponse = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddCompetitionGroup(
                        "Open",
                        false,
                        BigDecimal.ONE,
                        CompetitionGroup.ResultTimeType.RAW.toString()
                )));
        String openId = UnitTestUtils.getEntityIdFromResponse(addOpenResponse);
        Response addNoviceResponse = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddCompetitionGroup(
                        "Novice",
                        true,
                        BigDecimal.ONE,
                        CompetitionGroup.ResultTimeType.HANDICAP.toString()
                )));
        String addNoviceId = UnitTestUtils.getEntityIdFromResponse(addNoviceResponse);
        Response addProResponse = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddCompetitionGroup(
                        "Pro",
                        true,
                        BigDecimal.ONE,
                        CompetitionGroup.ResultTimeType.HANDICAP.toString()
                )));
        String proId = UnitTestUtils.getEntityIdFromResponse(addProResponse);

        URI competitionGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet(
                "2017 Competition Groups",
                Sets.newHashSet(openId, addNoviceId, proId)
        );
        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));

        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String competitionGroupSetId = UnitTestUtils.getEntityIdFromResponse(addCompetitionGroupSetResponseContainer);
        assertThat(competitionGroupSetId).isNotEmpty();
    }

    @Test
    public void whenAddCompetitionGroupToCompetitionGroupSetWithExistingCompetitionGroupsItShouldOk() {
        // add some initial competition groups
        URI competitionGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUPS_PATH)
                .build();
        AddCompetitionGroupRequest addOpenRequest = ApiRequestTestUtils.fullAddCompetitionGroup(
                "Open",
                false,
                BigDecimal.ONE,
                CompetitionGroup.ResultTimeType.RAW.toString()
        );
        Response addOpenResponse = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addOpenRequest));
        String openId = UnitTestUtils.getEntityIdFromResponse(addOpenResponse);
        AddCompetitionGroupRequest addNoviceRequest = ApiRequestTestUtils.fullAddCompetitionGroup(
                "Novice",
                true,
                BigDecimal.ONE,
                CompetitionGroup.ResultTimeType.HANDICAP.toString()
        );
        Response addNoviceResponse = client.target(competitionGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addNoviceRequest));
        String noviceId = UnitTestUtils.getEntityIdFromResponse(addNoviceResponse);
        URI competitionGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();

        // add a competition group set with only "Open"
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet(
                "2017 Competition Groups",
                Sets.newHashSet(openId)
        );
        Response addCompetitionGroupSetResponseContainer = client.target(competitionGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));
        assertThat(addCompetitionGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String competitionGroupSetId = UnitTestUtils.getEntityIdFromResponse(addCompetitionGroupSetResponseContainer);

        // add "Novice" to the competition group set
        URI addCompetitionGroupToSetUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SET_ADD_COMPETITION_GROUP_PATH)
                .build(competitionGroupSetId, noviceId);
        Response addCompetitionGroupToSetResponseContainer = client.target(addCompetitionGroupToSetUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(null);
        assertThat(addCompetitionGroupToSetResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        CompetitionGroupSetApiEntity actual = addCompetitionGroupToSetResponseContainer.readEntity(
                CompetitionGroupSetApiEntity.class
        );
        assertThat(actual.getCompetitionGroups().stream()
                .filter(competitionGroupApiEntity -> competitionGroupApiEntity.getId().equals(openId)))
                .hasSize(1);
        assertThat(actual.getCompetitionGroups().stream()
                .filter(competitionGroupApiEntity -> competitionGroupApiEntity.getId().equals(noviceId)))
                .hasSize(1);
    }

    @Test
    public void whenCreateInvalidCompetitionGroupSetItShouldRejectUnprocessable() {
        URI competitionGroupsSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(COMPETITION_GROUP_SETS_PATH)
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils
                .fullAddCompetitionGroupSet();
        addCompetitionGroupSetRequest.setName("  "); // whitespace only is not ok

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
                .fullAddCompetitionGroupSet();
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
