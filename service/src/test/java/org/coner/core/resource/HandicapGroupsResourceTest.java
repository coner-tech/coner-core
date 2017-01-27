package org.coner.core.resource;

import static org.coner.core.util.TestConstants.HANDICAP_GROUP_ID;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.response.ErrorsResponse;
import org.coner.core.api.response.GetHandicapGroupsResponse;
import org.coner.core.boundary.HandicapGroupApiAddPayloadBoundary;
import org.coner.core.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class HandicapGroupsResourceTest {
    private final HandicapGroupEntityService handicapGroupEntityService = mock(HandicapGroupEntityService.class);
    private final HandicapGroupApiDomainBoundary apiDomainBoundary = mock(HandicapGroupApiDomainBoundary.class);
    private final HandicapGroupApiAddPayloadBoundary apiAddPayloadBoundary = mock(
            HandicapGroupApiAddPayloadBoundary.class
    );
    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupsResource(
                    handicapGroupEntityService,
                    apiDomainBoundary,
                    apiAddPayloadBoundary
            ))
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(handicapGroupEntityService, apiDomainBoundary, apiAddPayloadBoundary);

        MockitoAnnotations.initMocks(this);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidHandicapGroupItShouldAddHandicapGroup() throws Exception {
        Response response = postHandicapGroup();

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo(HANDICAP_GROUP_ID);

        verify(handicapGroupEntityService).add(any(HandicapGroupAddPayload.class));
        verifyNoMoreInteractions(handicapGroupEntityService);
    }

    @Test
    public void whenAddHandicapGroupWithLargeFactorItShouldFailValidation() throws Exception {
        AddHandicapGroupRequest requestHandicapGroup = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request-wrong-factor.json"),
                AddHandicapGroupRequest.class
        );
        Entity<AddHandicapGroupRequest> requestEntity = Entity.json(requestHandicapGroup);

        HandicapGroupAddPayload addPayload = mock(HandicapGroupAddPayload.class);
        when(apiAddPayloadBoundary.toRemoteEntity(requestHandicapGroup)).thenReturn(addPayload);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors())
                .contains("handicapFactor must be less than or equal to 1.0000");

        verifyZeroInteractions(handicapGroupEntityService);
    }

    @Test
    public void itShouldGetHandicapGroups() throws Exception {
        List<HandicapGroup> domainHandicapGroups = new ArrayList<>();
        when(handicapGroupEntityService.getAll()).thenReturn(domainHandicapGroups);

        GetHandicapGroupsResponse response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetHandicapGroupsResponse.class);

        assertThat(response)
                .isNotNull();
        assertThat(response.getHandicapGroups())
                .isNotNull()
                .isEmpty();

        verify(handicapGroupEntityService).getAll();
        verify(apiDomainBoundary).toLocalEntities(domainHandicapGroups);
        verifyNoMoreInteractions(handicapGroupEntityService);
    }

    @Test
    public void itShouldGetPostedHandicapGroup() throws Exception {
        postHandicapGroup();

        List<HandicapGroup> domainHandicapGroups = new ArrayList<>();
        domainHandicapGroups.add(DomainEntityTestUtils.fullHandicapGroup());
        List<HandicapGroupApiEntity> handicapGroupApiEntities = new ArrayList<>();
        handicapGroupApiEntities.add(ApiEntityTestUtils.fullHandicapGroup());

        when(handicapGroupEntityService.getAll())
                .thenReturn(domainHandicapGroups);
        when(apiDomainBoundary.toLocalEntities(domainHandicapGroups))
                .thenReturn(handicapGroupApiEntities);

        GetHandicapGroupsResponse response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetHandicapGroupsResponse.class);

        verify(handicapGroupEntityService).getAll();
        verifyNoMoreInteractions(handicapGroupEntityService);

        assertThat(response)
                .isNotNull();
        assertThat(response.getHandicapGroups())
                .isNotNull()
                .hasSize(1);

    }

    private Response postHandicapGroup() throws Exception {
        AddHandicapGroupRequest request = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/handicap_group_add-request.json"),
                AddHandicapGroupRequest.class
        );
        Entity<AddHandicapGroupRequest> requestEntity = Entity.json(request);
        HandicapGroupAddPayload addPayload = mock(HandicapGroupAddPayload.class);
        when(apiAddPayloadBoundary.toRemoteEntity(request)).thenReturn(addPayload);
        HandicapGroup domainEntity = mock(HandicapGroup.class);
        when(handicapGroupEntityService.add(addPayload)).thenReturn(domainEntity);
        HandicapGroupApiEntity apiEntity = mock(HandicapGroupApiEntity.class);
        when(apiDomainBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);
        when(apiEntity.getId()).thenReturn(HANDICAP_GROUP_ID);

        Response response = resources.client()
                .target("/handicapGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(handicapGroupEntityService).add(addPayload);
        verifyNoMoreInteractions(handicapGroupEntityService);

        return response;
    }

}
