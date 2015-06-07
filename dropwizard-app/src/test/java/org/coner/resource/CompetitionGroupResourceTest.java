package org.coner.resource;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.boundary.CompetitionGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroup;
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
import static org.mockito.Mockito.verifyZeroInteractions;
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
    public void itShouldGetCompetitionGroup() {
        CompetitionGroup domainCompetitionGroup = DomainEntityTestUtils.fullCompetitionGroup();
        CompetitionGroupApiEntity competitionGroupApiEntity = ApiEntityTestUtils.fullCompetitionGroup();

        // sanity check test
        assertThat(domainCompetitionGroup.getId()).isSameAs(TestConstants.COMPETITION_GROUP_ID);
        assertThat(competitionGroupApiEntity.getId()).isSameAs(TestConstants.COMPETITION_GROUP_ID);

        when(conerCoreService.getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID))
                .thenReturn(domainCompetitionGroup);
        when(boundary.toLocalEntity(domainCompetitionGroup))
                .thenReturn(competitionGroupApiEntity);

        Response competitionGroupResourceContainer = resources.client()
                .target("/competitionGroups/" + TestConstants.COMPETITION_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID);
        verify(boundary).toLocalEntity(domainCompetitionGroup);
        verifyNoMoreInteractions(conerCoreService, boundary);

        assertThat(competitionGroupResourceContainer).isNotNull();
        assertThat(competitionGroupResourceContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        CompetitionGroupApiEntity getCompetitionGroupResponse = competitionGroupResourceContainer.readEntity(
                CompetitionGroupApiEntity.class
        );
        assertThat(getCompetitionGroupResponse)
                .isNotNull()
                .isEqualTo(competitionGroupApiEntity);
    }

    @Test
    public void itShouldResponseWithNotFoundWhenCompetitionGroupNotFound() {
        when(conerCoreService.getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID)).thenReturn(null);

        Response response = resources.client()
                .target("/competitionGroups/" + TestConstants.COMPETITION_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroup(TestConstants.COMPETITION_GROUP_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(boundary);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
