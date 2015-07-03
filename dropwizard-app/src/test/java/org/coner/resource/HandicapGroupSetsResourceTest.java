package org.coner.resource;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HandicapGroupSetsResourceTest {

    private ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private HandicapGroupSetApiDomainBoundary domainEntityBoundary = mock(HandicapGroupSetApiDomainBoundary.class);
    private HandicapGroupSetApiAddPayloadBoundary apiAddPayloadBoundary = mock(
            HandicapGroupSetApiAddPayloadBoundary.class
    );

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupSetsResource(conerCoreService, domainEntityBoundary, apiAddPayloadBoundary))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(domainEntityBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidHandicapGroupSetItShouldAdd() throws Exception {
        Response response = postHandicapGroupSet();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo(TestConstants.HANDICAP_GROUP_SET_ID);
    }

    private Response postHandicapGroupSet() throws Exception {
        AddHandicapGroupSetRequest request = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/request/handicap_group_set_add-request.json"),
                AddHandicapGroupSetRequest.class
        );
        Entity<AddHandicapGroupSetRequest> requestEntity = Entity.json(request);

        HandicapGroupSetAddPayload addPayload = mock(HandicapGroupSetAddPayload.class);
        when(apiAddPayloadBoundary.toRemoteEntity(request)).thenReturn(addPayload);
        HandicapGroupSet domainEntity = mock(HandicapGroupSet.class);
        when(conerCoreService.addHandicapGroupSet(addPayload)).thenReturn(domainEntity);
        HandicapGroupSetApiEntity apiEntity = mock(HandicapGroupSetApiEntity.class);
        when(domainEntityBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);
        when(apiEntity.getId()).thenReturn(TestConstants.HANDICAP_GROUP_SET_ID);

        Response response = resources.client()
                .target("/handicapGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addHandicapGroupSet(addPayload);

        return response;
    }
}
