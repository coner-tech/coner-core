package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.COMPETITION_GROUP_SET_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.mapper.CompetitionGroupSetMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class CompetitionGroupSetsResourceTest {

    private CompetitionGroupSetMapper competitionGroupSetMapper = mock(
            CompetitionGroupSetMapper.class
    );
    private CompetitionGroupSetService conerCoreService = mock(CompetitionGroupSetService.class);
    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupSetsResource(
                    conerCoreService,
                    competitionGroupSetMapper
            ))
            .build();

    @Before
    public void setup() {
        reset(competitionGroupSetMapper, conerCoreService);

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
        org.coner.core.api.request.AddCompetitionGroupSetRequest requestAddCompetitionGroupSet = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/request/competition_group_set_add-request.json"),
                org.coner.core.api.request.AddCompetitionGroupSetRequest.class
        );

        Entity<AddCompetitionGroupSetRequest> requestEntity = Entity.json(requestAddCompetitionGroupSet);

        CompetitionGroupSetAddPayload addPayload = mock(CompetitionGroupSetAddPayload.class);
        when(competitionGroupSetMapper.toAddPayload(requestAddCompetitionGroupSet)).thenReturn(addPayload);
        CompetitionGroupSet domainEntity = mock(CompetitionGroupSet.class);
        when(conerCoreService.add(addPayload)).thenReturn(domainEntity);
        CompetitionGroupSetApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroupSet();
        when(competitionGroupSetMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/competitionGroups/sets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).add(addPayload);
        verifyNoMoreInteractions(conerCoreService);

        return response;
    }
}
