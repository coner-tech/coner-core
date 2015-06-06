package org.coner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.HandicapGroup;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.JacksonUtil;
import org.coner.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class HandicapGroupSetsResourceTest {

    private HandicapGroupSetBoundary handicapGroupSetBoundary = mock(HandicapGroupSetBoundary.class);
    private ConerCoreService conerCoreService = mock(ConerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupSetsResource(handicapGroupSetBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(handicapGroupSetBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidHandicapGroupSetItShouldAdd() throws Exception {
        Response response = postHandicapGroupSet();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo("arbitrary-id-from-service");
    }

    private Response postHandicapGroupSet() throws Exception {
        AddHandicapGroupSetRequest requestAddHandicapGroupSet = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/request/handicap_group_set_add-request.json"),
                AddHandicapGroupSetRequest.class
        );

        Entity<AddHandicapGroupSetRequest> requestEntity = Entity.json(requestAddHandicapGroupSet);

        String handicapGroup1Id = "test-handicap-group-id-1";
        String handicapGroup2Id = "test-handicap-group-id-2";
        HandicapGroupSet requestHandicapGroupSetAsDomain = DomainEntityTestUtils.fullHandicapGroupSet(
                "arbitrary-id-from-service",
                "2015 Handicap Group Set",
                null

        );
        when(handicapGroupSetBoundary.toDomainEntity(requestAddHandicapGroupSet))
                .thenReturn(requestHandicapGroupSetAsDomain);
        HandicapGroup handicapGroup1 = DomainEntityTestUtils.fullHandicapGroup(
                handicapGroup1Id,
                "test handicap group 1",
                null
        );
        when(conerCoreService.getHandicapGroup(handicapGroup1Id)).thenReturn(handicapGroup1);

        final HandicapGroup handicapGroup2 = DomainEntityTestUtils.fullHandicapGroup(
                handicapGroup2Id,
                "test handicap group 2",
                null
        );
        when(conerCoreService.getHandicapGroup(handicapGroup2Id)).thenReturn(handicapGroup2);

        Response response = resources.client()
                .target("/handicapGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(handicapGroupSetBoundary).toDomainEntity(any(AddHandicapGroupSetRequest.class));
        verify(conerCoreService).getHandicapGroup(handicapGroup1Id);
        verify(conerCoreService).getHandicapGroup(handicapGroup2Id);
        ArgumentCaptor<HandicapGroupSet> handicapGroupSetCaptor = ArgumentCaptor
                .forClass(HandicapGroupSet.class);
        verify(conerCoreService).addHandicapGroupSet(handicapGroupSetCaptor.capture());
        HandicapGroupSet addedHandicapGroupSet = handicapGroupSetCaptor.getValue();
        assertThat(addedHandicapGroupSet.getId()).isNotEmpty();
        assertThat(addedHandicapGroupSet.getHandicapGroups()).hasSize(2);
        verifyNoMoreInteractions(conerCoreService, handicapGroupSetBoundary);

        return response;
    }
}
