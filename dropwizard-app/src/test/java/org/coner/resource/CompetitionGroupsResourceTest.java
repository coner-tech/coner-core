package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.api.request.AddCompetitionGroupRequest;
import org.coner.api.response.*;
import org.coner.boundary.*;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;
import org.mockito.MockitoAnnotations;

import static org.coner.util.TestConstants.COMPETITION_GROUP_ID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CompetitionGroupsResourceTest {

    private ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private CompetitionGroupApiDomainBoundary apiDomainEntityBoundary = mock(CompetitionGroupApiDomainBoundary.class);
    private CompetitionGroupApiAddPayloadBoundary apiAddPayloadBoundary = mock(
            CompetitionGroupApiAddPayloadBoundary.class
    );

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(
                    new CompetitionGroupsResource(
                            conerCoreService,
                            apiDomainEntityBoundary,
                            apiAddPayloadBoundary
                    )
            )
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(conerCoreService, apiDomainEntityBoundary, apiAddPayloadBoundary);

        MockitoAnnotations.initMocks(this);

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
        assertThat(UnitTestUtils.getEntityIdFromResponse(response)).isEqualTo(COMPETITION_GROUP_ID);
    }

    @Test
    public void whenAddCompetitionGroupWithLargeHandicapFactorItShouldFailValidation() throws Exception {
        AddCompetitionGroupRequest requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-with-large-handicap-factor.json"),
                AddCompetitionGroupRequest.class
        );
        Entity<AddCompetitionGroupRequest> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("handicapFactor must be less than or equal to 1.000");
        verifyZeroInteractions(conerCoreService);
    }

    @Test
    public void whenAddCompetitionGroupWithoutResultTimeTypeItShouldFailValidation() throws Exception {
        AddCompetitionGroupRequest requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-result-time-type.json"),
                AddCompetitionGroupRequest.class
        );
        Entity<AddCompetitionGroupRequest> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("resultTimeType may not be empty");
        verifyZeroInteractions(conerCoreService);
    }

    @Test
    public void whenAddCompetitionGroupWithoutGroupingItShouldFailValidation() throws Exception {
        AddCompetitionGroupRequest requestCompetitionGroupApiEntity = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request-without-grouping.json"),
                AddCompetitionGroupRequest.class
        );
        Entity<AddCompetitionGroupRequest> requestEntity = Entity.json(requestCompetitionGroupApiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("grouping may not be null");
        verifyZeroInteractions(conerCoreService);
    }

    @Test
    public void itShouldGetPostedCompetitionGroup() throws Exception {
        postCompetitionGroup();

        reset(conerCoreService, apiDomainEntityBoundary, apiAddPayloadBoundary);

        List<CompetitionGroup> domainEntities = Arrays.asList(DomainEntityTestUtils.fullCompetitionGroup());
        when(conerCoreService.getCompetitionGroups()).thenReturn(domainEntities);
        List<CompetitionGroupApiEntity> apiEntities = Arrays.asList(ApiEntityTestUtils.fullCompetitionGroup());
        when(apiDomainEntityBoundary.toLocalEntities(domainEntities)).thenReturn(apiEntities);

        GetCompetitionGroupsResponse response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetCompetitionGroupsResponse.class);

        verify(conerCoreService).getCompetitionGroups();
        verifyNoMoreInteractions(conerCoreService);

        assertThat(response)
                .isNotNull();
        assertThat(response.getCompetitionGroups())
                .isNotNull()
                .hasSize(1);
    }

    private Response postCompetitionGroup() throws Exception {
        AddCompetitionGroupRequest request = objectMapper.readValue(
                fixture("fixtures/api/entity/competition_group_add-request.json"),
                AddCompetitionGroupRequest.class
        );
        Entity<AddCompetitionGroupRequest> requestEntity = Entity.json(request);
        CompetitionGroupAddPayload addPayload = mock(CompetitionGroupAddPayload.class);
        when(apiAddPayloadBoundary.toRemoteEntity(request)).thenReturn(addPayload);
        CompetitionGroup domainEntity = mock(CompetitionGroup.class);
        when(conerCoreService.addCompetitionGroup(addPayload)).thenReturn(domainEntity);
        CompetitionGroupApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroup();
        when(apiDomainEntityBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addCompetitionGroup(addPayload);
        verifyNoMoreInteractions(conerCoreService);

        return response;
    }
}
