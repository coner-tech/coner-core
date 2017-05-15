package org.coner.core.resource;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.COMPETITION_GROUP_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.response.GetCompetitionGroupsResponse;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.service.CompetitionGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.junit.ResourceTestRule;

public class CompetitionGroupsResourceTest {

    private final CompetitionGroupEntityService competitionGroupEntityService = mock(
            CompetitionGroupEntityService.class
    );
    private final CompetitionGroupMapper competitionGroupMapper = mock(CompetitionGroupMapper.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(
                    new CompetitionGroupsResource(
                            competitionGroupEntityService,
                            competitionGroupMapper
                    )
            )
            .addResource(new DomainServiceExceptionMapper())
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(competitionGroupEntityService, competitionGroupMapper);

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
        assertThat(response.getHeaderString(HttpHeader.LOCATION.asString()))
                .containsSequence("/competitionGroups/", COMPETITION_GROUP_ID);
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
        ValidationErrorMessage errorsResponse = response.readEntity(ValidationErrorMessage.class);
        assertThat(errorsResponse.getErrors())
                .isNotEmpty()
                .contains("handicapFactor must be less than or equal to 1.000");
        verifyZeroInteractions(competitionGroupEntityService);
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
        ValidationErrorMessage validationErrorMessage = response.readEntity(ValidationErrorMessage.class);
        assertThat(validationErrorMessage.getErrors())
                .isNotEmpty()
                .contains("resultTimeType may not be empty");
        verifyZeroInteractions(competitionGroupEntityService);
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
        ValidationErrorMessage errorMessage = response.readEntity(ValidationErrorMessage.class);
        assertThat(errorMessage.getErrors())
                .isNotEmpty()
                .contains("grouping may not be null");
        verifyZeroInteractions(competitionGroupEntityService);
    }

    @Test
    public void itShouldGetPostedCompetitionGroup() throws Exception {
        postCompetitionGroup();

        reset(competitionGroupEntityService, competitionGroupMapper);

        List<CompetitionGroup> domainEntities = Arrays.asList(DomainEntityTestUtils.fullCompetitionGroup());
        when(competitionGroupEntityService.getAll()).thenReturn(domainEntities);
        List<CompetitionGroupApiEntity> apiEntities = Arrays.asList(ApiEntityTestUtils.fullCompetitionGroup());
        when(competitionGroupMapper.toApiEntityList(domainEntities)).thenReturn(apiEntities);

        GetCompetitionGroupsResponse response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetCompetitionGroupsResponse.class);

        verify(competitionGroupEntityService).getAll();
        verifyNoMoreInteractions(competitionGroupEntityService);

        assertThat(response)
                .isNotNull();
        assertThat(response.getEntities())
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
        when(competitionGroupMapper.toDomainAddPayload(request)).thenReturn(addPayload);
        CompetitionGroup domainEntity = mock(CompetitionGroup.class);
        when(competitionGroupEntityService.add(addPayload)).thenReturn(domainEntity);
        CompetitionGroupApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroup();
        when(competitionGroupMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/competitionGroups")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(competitionGroupEntityService).add(addPayload);
        verifyNoMoreInteractions(competitionGroupEntityService);

        return response;
    }

    @Test
    public void itShouldGetCompetitionGroup() throws Exception {
        final String competitionGroupId = TestConstants.COMPETITION_GROUP_ID;
        CompetitionGroup domainEntity = mock(CompetitionGroup.class);
        when(competitionGroupEntityService.getById(competitionGroupId)).thenReturn(domainEntity);
        CompetitionGroupApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroup();
        when(competitionGroupMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response competitionGroupResourceContainer = resources.client()
                .target("/competitionGroups/" + competitionGroupId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(competitionGroupEntityService).getById(competitionGroupId);
        verifyNoMoreInteractions(competitionGroupEntityService);

        assertThat(competitionGroupResourceContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        CompetitionGroupApiEntity getCompetitionGroupResponse = competitionGroupResourceContainer.readEntity(
                CompetitionGroupApiEntity.class
        );
        assertThat(getCompetitionGroupResponse).isEqualTo(apiEntity);
    }

    @Test
    public void itShouldRespondWithNotFoundWhenCompetitionGroupNotFound() throws Exception {
        final String competitionGroupId = TestConstants.COMPETITION_GROUP_ID;
        when(competitionGroupEntityService.getById(competitionGroupId)).thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/competitionGroups/" + competitionGroupId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(competitionGroupEntityService).getById(competitionGroupId);
        verifyNoMoreInteractions(competitionGroupEntityService);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
