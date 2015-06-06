package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.response.*;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.CompetitionGroup;
import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CompetitionGroupsResourceTest {

    private CompetitionGroupBoundary competitionGroupBoundary = mock(CompetitionGroupBoundary.class);
    private ConerCoreService conerCoreService = mock(ConerCoreService.class);
    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupsResource(competitionGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(competitionGroupBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidCompetitionGroupItShouldAddCompetitionGroup() throws Exception {
        Response response = postCompetitionGroup();

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo("arbitrary-id-from-service");
    }

    @Test
    public void whenAddCompetitionGroupWithUserSuppliedIdItShouldFailValidation() throws Exception {
        CompetitionGroupApiEntity requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-with-id.json"),
                CompetitionGroupApiEntity.class
        );
        Entity<CompetitionGroupApiEntity> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("id competitionGroup.id may only be assigned by the system (was bad-id-in-request)");

        verify(conerCoreService, never()).addCompetitionGroup(any(org.coner.core.domain.CompetitionGroup.class));
    }

    @Test
    public void whenAddCompetitionGroupWithLargeHandicapFactorItShouldFailValidation() throws Exception {
        CompetitionGroupApiEntity requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-with-large-handicap-factor.json"),
                CompetitionGroupApiEntity.class
        );
        Entity<CompetitionGroupApiEntity> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("handicapFactor must be less than or equal to 1.000 (was 1.0001)");

        verify(conerCoreService, never()).addCompetitionGroup(any(CompetitionGroup.class));
    }

    @Test
    public void whenAddCompetitionGroupWithoutResultTimeTypeItShouldFailValidation() throws Exception {
        CompetitionGroupApiEntity requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-result-time-type.json"),
                CompetitionGroupApiEntity.class
        );
        Entity<CompetitionGroupApiEntity> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("resultTimeType may not be empty (was null)");

        verify(conerCoreService, never()).addCompetitionGroup(any(CompetitionGroup.class));
    }

    @Test
    public void whenAddCompetitionGroupWithoutGroupingItShouldFailValidation() throws Exception {
        CompetitionGroupApiEntity requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-grouping.json"),
                CompetitionGroupApiEntity.class
        );
        Entity<CompetitionGroupApiEntity> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("grouping may not be null (was null)");

        verify(conerCoreService, never()).addCompetitionGroup(any(CompetitionGroup.class));
    }

    @Test
    public void itShouldGetPostedCompetitionGroup() throws Exception {
        postCompetitionGroup();

        List<CompetitionGroup> domainCompetitionGroups = new ArrayList<>();
        domainCompetitionGroups.add(DomainEntityTestUtils.fullCompetitionGroup());
        List<CompetitionGroupApiEntity> competitionGroupApiEntities = new ArrayList<>();
        competitionGroupApiEntities.add(ApiEntityTestUtils.fullCompetitionGroup());

        when(conerCoreService.getCompetitionGroups())
                .thenReturn(domainCompetitionGroups);
        when(competitionGroupBoundary.toApiEntities(domainCompetitionGroups))
                .thenReturn(competitionGroupApiEntities);

        GetCompetitionGroupsResponse response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetCompetitionGroupsResponse.class);

        verify(conerCoreService).getCompetitionGroups();
        verify(competitionGroupBoundary).toApiEntities(domainCompetitionGroups);
        verifyNoMoreInteractions(conerCoreService, competitionGroupBoundary);

        assertThat(response)
                .isNotNull();
        assertThat(response.getCompetitionGroups())
                .isNotNull()
                .hasSize(1);
    }

    private Response postCompetitionGroup() throws Exception {
        CompetitionGroupApiEntity requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request.json"),
                CompetitionGroupApiEntity.class
        );

        Entity<CompetitionGroupApiEntity> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        org.coner.core.domain.CompetitionGroup requestCompetitionGroupAsDomain =
                new org.coner.core.domain.CompetitionGroup();
        requestCompetitionGroupAsDomain.setId("arbitrary-id-from-service");

        when(competitionGroupBoundary.toDomainEntity(requestCompetitionGroupApiEntity))
                .thenReturn(requestCompetitionGroupAsDomain);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(competitionGroupBoundary).toDomainEntity(requestCompetitionGroupApiEntity);
        verify(conerCoreService).addCompetitionGroup(requestCompetitionGroupAsDomain);
        verifyNoMoreInteractions(conerCoreService, competitionGroupBoundary);

        return response;
    }
}
