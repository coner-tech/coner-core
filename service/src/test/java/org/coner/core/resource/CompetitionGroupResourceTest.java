package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.service.CompetitionGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;

public class CompetitionGroupResourceTest {

    private final CompetitionGroupMapper competitionGroupMapper = mock(CompetitionGroupMapper.class);
    private final CompetitionGroupEntityService competitionGroupEntityService = mock(
            CompetitionGroupEntityService.class
    );

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupResource(competitionGroupMapper, competitionGroupEntityService))
            .addResource(new DomainServiceExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(competitionGroupMapper, competitionGroupEntityService);
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
