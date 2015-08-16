package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.boundary.CompetitionGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.util.*;

import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CompetitionGroupResourceTest {

    private final CompetitionGroupApiDomainBoundary boundary = mock(CompetitionGroupApiDomainBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupResource(boundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(boundary, conerCoreService);
    }

    @Test
    public void itShouldGetCompetitionGroup() throws Exception {
        final String competitionGroupId = TestConstants.COMPETITION_GROUP_ID;
        CompetitionGroup domainEntity = mock(CompetitionGroup.class);
        when(conerCoreService.getCompetitionGroup(competitionGroupId)).thenReturn(domainEntity);
        CompetitionGroupApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroup();
        when(boundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response competitionGroupResourceContainer = resources.client()
                .target("/competitionGroups/" + competitionGroupId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(competitionGroupId);
        verifyNoMoreInteractions(conerCoreService);

        assertThat(competitionGroupResourceContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        CompetitionGroupApiEntity getCompetitionGroupResponse = competitionGroupResourceContainer.readEntity(
                CompetitionGroupApiEntity.class
        );
        assertThat(getCompetitionGroupResponse).isEqualTo(apiEntity);
    }

    @Test
    public void itShouldRespondWithNotFoundWhenCompetitionGroupNotFound() throws Exception {
        final String competitionGroupId = TestConstants.COMPETITION_GROUP_ID;
        when(conerCoreService.getCompetitionGroup(competitionGroupId)).thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/competitionGroups/" + competitionGroupId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(competitionGroupId);
        verifyNoMoreInteractions(conerCoreService);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
