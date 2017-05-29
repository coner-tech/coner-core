package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.response.GetHandicapGroupSetsResponse;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestUtils;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.jersey.validation.ValidationErrorMessage;

public class HandicapGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String HANDICAP_GROUPS_PATH = "/handicapGroups";
    private static final String HANDICAP_GROUP_PATH = "/handicapGroups/{handicapGroupId}";
    private static final String HANDICAP_GROUP_SETS_PATH = "/handicapGroups/sets";
    private static final String HANDICAP_GROUP_SET_ADD_COMPETITION_GROUP_PATH =
            "/handicapGroups/sets/{handicapGroupSetId}/handicapGroups/{handicapGroupId}";

    @Test
    public void whenCreateHandicapGroupItShouldPersist() {
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUPS_PATH)
                .build();
        AddHandicapGroupRequest addHandicapGroupRequest = ApiRequestTestUtils.fullAddHandicapGroup();
        addHandicapGroupRequest.setName(TestConstants.HANDICAP_GROUP_NAME);
        addHandicapGroupRequest.setFactor(TestConstants.HANDICAP_GROUP_FACTOR);

        Response addHandicapGroupResponseContainer = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupRequest));

        assertThat(addHandicapGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String handicapGroupId = UnitTestUtils.getEntityIdFromResponse(addHandicapGroupResponseContainer);
        assertThat(handicapGroupId).isNotNull();

        URI handicapGroupUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_PATH)
                .build(handicapGroupId);

        Response getHandicapGroupResponseContainer = client.target(handicapGroupUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getHandicapGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        HandicapGroupApiEntity getHandicapGroupResponse = getHandicapGroupResponseContainer
                .readEntity(HandicapGroupApiEntity.class);
        assertThat(getHandicapGroupResponse.getId()).isEqualTo(handicapGroupId);
    }

    @Test
    public void whenCreateInvalidHandicapGroupItShouldReject() {
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUPS_PATH)
                .build();
        AddHandicapGroupRequest addHandicapGroupRequest = ApiRequestTestUtils.fullAddHandicapGroup();
        addHandicapGroupRequest.setName(null);
        addHandicapGroupRequest.setFactor(null);

        Response addHandicapGroupResponseContainer = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupRequest));

        assertThat(addHandicapGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ValidationErrorMessage validationErrorMessage = addHandicapGroupResponseContainer.readEntity(
                ValidationErrorMessage.class
        );
        assertThat(validationErrorMessage.getErrors()).isNotEmpty();
    }

    @Test
    public void whenCreateHandicapGroupSetWithEmptyIdsItShouldPersist() {
        URI handicapGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils.fullAddHandicapGroupSet();
        addHandicapGroupSetRequest.setHandicapGroupIds(null); // perfectly ok to create an empty one

        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));

        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String handicapGroupSetId = UnitTestUtils.getEntityIdFromResponse(addHandicapGroupSetResponseContainer);
        assertThat(handicapGroupSetId).isNotEmpty();
    }

    @Test
    public void whenCreateHandicapGroupSetWithMultipleIdsItShouldPersist() {
        // add some initial handicap group IDs
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUPS_PATH)
                .build();
        Response addSsResponse = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddHandicapGroup("SS", BigDecimal.valueOf(0.826d))));
        String ssId = UnitTestUtils.getEntityIdFromResponse(addSsResponse);
        Response addAsResponse = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddHandicapGroup("AS", BigDecimal.valueOf(0.819d))));
        String asId = UnitTestUtils.getEntityIdFromResponse(addAsResponse);
        Response addBsResponse = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(ApiRequestTestUtils.fullAddHandicapGroup("BS", BigDecimal.valueOf(0.813d))));
        String bsId = UnitTestUtils.getEntityIdFromResponse(addBsResponse);

        URI handicapGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils.fullAddHandicapGroupSet(
                "2017 PAX/RTP INDEX",
                Sets.newHashSet(ssId, asId, bsId)
        );
        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));

        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String handicapGroupSetId = UnitTestUtils.getEntityIdFromResponse(addHandicapGroupSetResponseContainer);
        assertThat(handicapGroupSetId).isNotEmpty();
    }


    @Test
    public void whenAddHandicapGroupToHandicapGroupSetWithExistingHandicapGroupsItShouldOk() {
        // add some initial handicap groups
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUPS_PATH)
                .build();
        AddHandicapGroupRequest addAmRequest = ApiRequestTestUtils.fullAddHandicapGroup(
                "AM",
                BigDecimal.valueOf(1.000d)
        );
        Response addAmResponse = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addAmRequest));
        String amId = UnitTestUtils.getEntityIdFromResponse(addAmResponse);
        AddHandicapGroupRequest addBmRequest = ApiRequestTestUtils.fullAddHandicapGroup(
                "BM",
                BigDecimal.valueOf(0.956d)
        );
        Response addBmResponse = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addBmRequest));
        String bmId = UnitTestUtils.getEntityIdFromResponse(addBmResponse);


        // add a handicap group set with only "AM"
        URI handicapGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils.fullAddHandicapGroupSet(
                "2017 RTP/PAX Index",
                Sets.newHashSet(amId)
        );
        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));
        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String handicapGroupSetId = UnitTestUtils.getEntityIdFromResponse(addHandicapGroupSetResponseContainer);

        // add "BM" to the handicap group set
        URI addHandicapGroupToSetUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SET_ADD_COMPETITION_GROUP_PATH)
                .build(handicapGroupSetId, bmId);
        Response addHandicapGroupToSetResponseContainer = client.target(addHandicapGroupToSetUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(null);
        assertThat(addHandicapGroupToSetResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        HandicapGroupSetApiEntity actual = addHandicapGroupToSetResponseContainer.readEntity(
                HandicapGroupSetApiEntity.class
        );
        assertThat(actual.getHandicapGroups().stream()
                .filter(handicapGroupApiEntity -> handicapGroupApiEntity.getId().equals(amId)))
                .hasSize(1);
        assertThat(actual.getHandicapGroups().stream()
                .filter(handicapGroupApiEntity -> handicapGroupApiEntity.getId().equals(bmId)))
                .hasSize(1);
    }


    @Test
    public void whenCreateInvalidHandicapGroupSetItShouldRejectUnprocessable() {
        URI handicapGroupsSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils.fullAddHandicapGroupSet();
        addHandicapGroupSetRequest.setName("  "); // whitespace only is not ok

        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupsSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));

        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    @Test
    public void whenGetAllHandicapGroupSetsItShouldReturnIt() {
        URI handicapGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils
                .fullAddHandicapGroupSet();
        addHandicapGroupSetRequest.setHandicapGroupIds(null);

        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));
        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);

        Response getHandicapGroupSetsResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();
        GetHandicapGroupSetsResponse actual = getHandicapGroupSetsResponseContainer
                .readEntity(GetHandicapGroupSetsResponse.class);
        assertThat(actual.getEntities()).isNotEmpty();
        assertThat(actual.getEntities().get(0).getId()).isNotEmpty();
    }

}
