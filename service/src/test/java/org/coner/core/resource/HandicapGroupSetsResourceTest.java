package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.mapper.HandicapGroupSetMapper;
import org.coner.core.util.JacksonUtil;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class HandicapGroupSetsResourceTest {

    HandicapGroupSetService handicapGroupSetService = mock(HandicapGroupSetService.class);
    HandicapGroupSetMapper handicapGroupSetMapper = mock(HandicapGroupSetMapper.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupSetsResource(
                    handicapGroupSetService,
                    handicapGroupSetMapper
            ))
            .build();

    @Before
    public void setup() {
        reset(handicapGroupSetService, handicapGroupSetMapper);

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
        when(handicapGroupSetMapper.toDomainAddPayload(request)).thenReturn(addPayload);
        HandicapGroupSet domainEntity = mock(HandicapGroupSet.class);
        when(handicapGroupSetService.add(addPayload)).thenReturn(domainEntity);
        HandicapGroupSetApiEntity apiEntity = mock(HandicapGroupSetApiEntity.class);
        when(handicapGroupSetMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);
        when(apiEntity.getId()).thenReturn(TestConstants.HANDICAP_GROUP_SET_ID);

        Response response = resources.client()
                .target("/handicapGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(handicapGroupSetService).add(addPayload);

        return response;
    }
}
