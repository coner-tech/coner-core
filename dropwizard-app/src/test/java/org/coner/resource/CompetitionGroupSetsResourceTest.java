package org.coner.resource;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
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

import static org.coner.util.TestConstants.COMPETITION_GROUP_SET_ID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CompetitionGroupSetsResourceTest {

    private CompetitionGroupSetApiDomainBoundary apiDomainBoundary = mock(
            CompetitionGroupSetApiDomainBoundary.class
    );
    private CompetitionGroupSetApiAddPayloadBoundary addPayloadBoundary = mock(
            CompetitionGroupSetApiAddPayloadBoundary.class
    );
    private ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupSetsResource(
                    conerCoreService,
                    apiDomainBoundary,
                    addPayloadBoundary
            ))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(apiDomainBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidCompetitionGroupSetItShouldAdd() throws Exception {
        Response response = postCompetitionGroupSet();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo(COMPETITION_GROUP_SET_ID);
    }

    private Response postCompetitionGroupSet() throws Exception {
        org.coner.api.request.AddCompetitionGroupSetRequest requestAddCompetitionGroupSet = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/request/competition_group_set_add-request.json"),
                org.coner.api.request.AddCompetitionGroupSetRequest.class
        );

        Entity<AddCompetitionGroupSetRequest> requestEntity = Entity.json(requestAddCompetitionGroupSet);

        CompetitionGroupSetAddPayload addPayload = mock(CompetitionGroupSetAddPayload.class);
        when(addPayloadBoundary.toRemoteEntity(requestAddCompetitionGroupSet)).thenReturn(addPayload);
        CompetitionGroupSet domainEntity = mock(CompetitionGroupSet.class);
        when(conerCoreService.addCompetitionGroupSet(addPayload)).thenReturn(domainEntity);
        CompetitionGroupSetApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroupSet();
        when(apiDomainBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/competitionGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addCompetitionGroupSet(addPayload);
        verifyNoMoreInteractions(conerCoreService);

        return response;
    }
}
