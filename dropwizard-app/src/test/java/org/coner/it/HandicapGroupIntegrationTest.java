package org.coner.it;

import org.coner.api.request.AddHandicapGroupRequest;
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

    @Test
    public void whenCreateEventItShouldPersist() {
        URI handicapGroupsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/handicapGroups")
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
    }

}
