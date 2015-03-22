package org.coner.it;

import org.coner.api.entity.HandicapGroup;
import org.coner.api.request.AddHandicapGroupRequest;
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
public class HandicapGroupIntegrationTest extends AbstractIntegrationTest {

    private static final String HANDICAP_GROUPS_PATH = "/handicapGroups";
    private static final String HANDICAP_GROUP_PATH = "/handicapGroups/{handicapGroupId}";

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
        HandicapGroup getHandicapGroupResponse = getHandicapGroupResponseContainer
                .readEntity(HandicapGroup.class);
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

}
