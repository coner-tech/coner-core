package org.coner.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.util.TestConstants.COMPETITION_GROUP_SET_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.boundary.CompetitionGroupSetApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.dropwizard.testing.junit.ResourceTestRule;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionGroupSetResourceTest {

    private final CompetitionGroupSetApiDomainBoundary boundary = mock(CompetitionGroupSetApiDomainBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CompetitionGroupSetResource(conerCoreService, boundary))
            .build();

    @Before
    public void setup() {
        reset(boundary, conerCoreService);
    }

    @Test
    public void itShouldGetCompetitionGroupSet() throws EntityNotFoundException {
        CompetitionGroupSet domainEntity = DomainEntityTestUtils.fullCompetitionGroupSet();
        CompetitionGroupSetApiEntity apiEntity = ApiEntityTestUtils.fullCompetitionGroupSet();

        // sanity check test
        assertThat(domainEntity.getId()).isSameAs(COMPETITION_GROUP_SET_ID);
        assertThat(apiEntity.getId()).isSameAs(COMPETITION_GROUP_SET_ID);

        when(conerCoreService.getCompetitionGroupSet(COMPETITION_GROUP_SET_ID)).thenReturn(domainEntity);
        when(boundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response responseContainer = resources.client()
                .target("/competitionGroups/sets/" + COMPETITION_GROUP_SET_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroupSet(COMPETITION_GROUP_SET_ID);
        verify(boundary).toLocalEntity(domainEntity);
        verifyNoMoreInteractions(conerCoreService, boundary);

        assertThat(responseContainer).isNotNull();
        assertThat(responseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        CompetitionGroupSetApiEntity response = responseContainer.readEntity(
                CompetitionGroupSetApiEntity.class
        );
        assertThat(response)
                .isNotNull()
                .isEqualTo(apiEntity);
    }

    @Test
    public void itShouldRespondNotFoundWhenNotFound() throws EntityNotFoundException {
        when(conerCoreService.getCompetitionGroupSet(COMPETITION_GROUP_SET_ID))
                .thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/competitionGroups/sets/" + COMPETITION_GROUP_SET_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getCompetitionGroupSet(COMPETITION_GROUP_SET_ID);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
