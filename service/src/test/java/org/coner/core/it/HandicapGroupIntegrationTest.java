package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.response.ErrorsResponse;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

public class HandicapGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String HANDICAP_GROUPS_PATH = "/handicapGroups";
    private static final String HANDICAP_GROUP_PATH = "/handicapGroups/{handicapGroupId}";
    private static final String HANDICAP_GROUP_SETS_PATH = "/handicapGroups/sets";

    @Test
    public void whenCreateHandicapGroupItShouldPersist() {
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUPS_PATH)
                .build();
        AddHandicapGroupRequest addHandicapGroupRequest = new AddHandicapGroupRequest();
        addHandicapGroupRequest.setName(TestConstants.HANDICAP_GROUP_NAME);
        addHandicapGroupRequest.setHandicapFactor(TestConstants.HANDICAP_GROUP_FACTOR);

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
        AddHandicapGroupRequest addHandicapGroupRequest = new AddHandicapGroupRequest();

        Response addHandicapGroupResponseContainer = client.target(handicapGroupsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupRequest));

        assertThat(addHandicapGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = addHandicapGroupResponseContainer.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
    }

    @Test
    public void whenCreateHandicapGroupSetItShouldPersist() {
        URI handicapGroupSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = new AddHandicapGroupSetRequest();
        addHandicapGroupSetRequest.setName(TestConstants.HANDICAP_GROUP_SET_NAME);
        addHandicapGroupSetRequest.setHandicapGroups(null); // perfectly ok to create an empty one

        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));

        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        String handicapGroupSetId = UnitTestUtils.getEntityIdFromResponse(addHandicapGroupSetResponseContainer);
        assertThat(handicapGroupSetId).isNotEmpty();
    }

    @Test
    public void whenCreateInvalidHandicapGroupSetItShouldRejectUnprocessable() {
        URI handicapGroupsSetsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path(HANDICAP_GROUP_SETS_PATH)
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = new AddHandicapGroupSetRequest();
        addHandicapGroupSetRequest.setName("  "); // whitespace only is not ok
        addHandicapGroupSetRequest.setHandicapGroups(null);

        Response addHandicapGroupSetResponseContainer = client.target(handicapGroupsSetsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));

        assertThat(addHandicapGroupSetResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

}
