package org.coner.resource;

import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.*;
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
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CompetitionGroupSetsResourceTest {

    private CompetitionGroupSetApiDomainBoundary competitionGroupSetBoundary = mock(
            CompetitionGroupSetApiDomainBoundary.class
    );
    private ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupSetsResource(competitionGroupSetBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(competitionGroupSetBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidCompetitionGroupSetItShouldAdd() throws Exception {
        Response response = postCompetitionGroupSet();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo("arbitrary-id-from-service");
    }

    private Response postCompetitionGroupSet() throws Exception {
        org.coner.api.request.AddCompetitionGroupSetRequest requestAddCompetitionGroupSet = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/request/competition_group_set_add-request.json"),
                org.coner.api.request.AddCompetitionGroupSetRequest.class
        );

        Entity<AddCompetitionGroupSetRequest> requestEntity = Entity.json(requestAddCompetitionGroupSet);

        CompetitionGroupSet requestCompetitionGroupSetAsDomain = new CompetitionGroupSet();
        requestCompetitionGroupSetAsDomain.setId("arbitrary-id-from-service");
        when(competitionGroupSetBoundary.toRemoteEntity(requestAddCompetitionGroupSet))
                .thenReturn(requestCompetitionGroupSetAsDomain);

        CompetitionGroup competitionGroup1 = new CompetitionGroup();
        competitionGroup1.setId("test-competition-group-id-1");
        competitionGroup1.setName("test competition group 1");
        competitionGroup1.setGrouping(false);
        competitionGroup1.setResultTimeType(CompetitionGroup.ResultTimeType.RAW);
        when(conerCoreService.getCompetitionGroup("test-competition-group-id-1")).thenReturn(competitionGroup1);

        CompetitionGroup competitionGroup2 = new CompetitionGroup();
        competitionGroup2.setId("test-competition-group-id-2");
        competitionGroup2.setName("test competition group 2");
        competitionGroup2.setGrouping(true);
        competitionGroup2.setResultTimeType(CompetitionGroup.ResultTimeType.HANDICAP);
        when(conerCoreService.getCompetitionGroup("test-competition-group-id-2")).thenReturn(competitionGroup2);

        Response response = resources.client()
                .target("/competitionGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(competitionGroupSetBoundary).toRemoteEntity(any(AddCompetitionGroupSetRequest.class));
        verify(conerCoreService).getCompetitionGroup("test-competition-group-id-1");
        verify(conerCoreService).getCompetitionGroup("test-competition-group-id-2");
        ArgumentCaptor<CompetitionGroupSet> competitionGroupSetCaptor = ArgumentCaptor
                .forClass(CompetitionGroupSet.class);
        verify(conerCoreService).addCompetitionGroupSet(competitionGroupSetCaptor.capture());
        CompetitionGroupSet addedCompetitionGroupSet = competitionGroupSetCaptor.getValue();
        assertThat(addedCompetitionGroupSet.getId()).isNotEmpty();
        assertThat(addedCompetitionGroupSet.getCompetitionGroups()).hasSize(2);
        verifyNoMoreInteractions(conerCoreService, competitionGroupSetBoundary);

        return response;
    }
}
