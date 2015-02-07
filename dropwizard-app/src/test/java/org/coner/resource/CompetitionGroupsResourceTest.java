package org.coner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.response.ErrorsResponse;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.CompetitionGroup;
import org.coner.util.JacksonUtil;
import org.coner.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class CompetitionGroupsResourceTest {

    private CompetitionGroupBoundary competitionGroupBoundary = mock(CompetitionGroupBoundary.class);
    private ConerCoreService conerCoreService = mock(ConerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupsResource(competitionGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(competitionGroupBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void whenAddValidCompetitionGroupItShouldAddCompetitionGroup() throws Exception {
        org.coner.api.entity.CompetitionGroup requestCompetitionGroup = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request.json"),
                org.coner.api.entity.CompetitionGroup.class
        );

        Entity<org.coner.api.entity.CompetitionGroup> requestEntity = Entity.json(requestCompetitionGroup);

        org.coner.core.domain.CompetitionGroup requestCompetitionGroupAsDomain =
                new org.coner.core.domain.CompetitionGroup();
        requestCompetitionGroupAsDomain.setId("arbitrary-id-from-service");

        when(competitionGroupBoundary.toDomainEntity(requestCompetitionGroup))
                .thenReturn(requestCompetitionGroupAsDomain);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addCompetitionGroup(requestCompetitionGroupAsDomain);
        verifyNoMoreInteractions(conerCoreService);

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
        assertThat(response.getHeaders().get("Location").get(0)).isNotNull();
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo("arbitrary-id-from-service");
    }

    @Test
    public void whenAddCompetitionGroupWithUserSuppliedIdItShouldFailValidation() throws Exception {
        org.coner.api.entity.CompetitionGroup requestCompetitionGroup = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-with-id.json"),
                org.coner.api.entity.CompetitionGroup.class
        );
        Entity<org.coner.api.entity.CompetitionGroup> requestEntity = Entity.json(requestCompetitionGroup);

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
        org.coner.api.entity.CompetitionGroup requestCompetitionGroup = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-with-large-handicap-factor.json"),
                org.coner.api.entity.CompetitionGroup.class
        );
        Entity<org.coner.api.entity.CompetitionGroup> requestEntity = Entity.json(requestCompetitionGroup);

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
        org.coner.api.entity.CompetitionGroup requestCompetitionGroup = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-result-time-type.json"),
                org.coner.api.entity.CompetitionGroup.class
        );
        Entity<org.coner.api.entity.CompetitionGroup> requestEntity = Entity.json(requestCompetitionGroup);

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
        org.coner.api.entity.CompetitionGroup requestCompetitionGroup = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-grouping.json"),
                org.coner.api.entity.CompetitionGroup.class
        );
        Entity<org.coner.api.entity.CompetitionGroup> requestEntity = Entity.json(requestCompetitionGroup);

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
}
