package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.HANDICAP_GROUP_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;

public class HandicapGroupResourceTest {

    private final HandicapGroupApiDomainBoundary handicapGroupBoundary = mock(HandicapGroupApiDomainBoundary.class);
    private final HandicapGroupEntityService handicapGroupEntityService = mock(HandicapGroupEntityService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupResource(handicapGroupBoundary, handicapGroupEntityService))
            .build();

    @Before
    public void setup() {
        reset(handicapGroupBoundary, handicapGroupEntityService);
    }

    @Test
    public void itShouldGetHandicapGroup() throws EntityNotFoundException {
        HandicapGroup domainHandicapGroup = DomainEntityTestUtils.fullHandicapGroup();
        HandicapGroupApiEntity handicapGroupApiEntity = ApiEntityTestUtils.fullHandicapGroup();

        // sanity check test
        assertThat(domainHandicapGroup.getId()).isSameAs(HANDICAP_GROUP_ID);
        assertThat(handicapGroupApiEntity.getId()).isSameAs(HANDICAP_GROUP_ID);

        when(handicapGroupEntityService.getById(HANDICAP_GROUP_ID)).thenReturn(domainHandicapGroup);
        when(handicapGroupBoundary.toLocalEntity(domainHandicapGroup)).thenReturn(handicapGroupApiEntity);

        Response responseContainer = resources.client()
                .target("/handicapGroups/" + HANDICAP_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(handicapGroupEntityService).getById(HANDICAP_GROUP_ID);
        verify(handicapGroupBoundary).toLocalEntity(domainHandicapGroup);
        verifyNoMoreInteractions(handicapGroupEntityService, handicapGroupBoundary);

        assertThat(responseContainer).isNotNull();
        assertThat(responseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        HandicapGroupApiEntity getHandicapGroupResponse = responseContainer.readEntity(HandicapGroupApiEntity.class);
        assertThat(getHandicapGroupResponse)
                .isNotNull()
                .isEqualTo(handicapGroupApiEntity);
    }

    @Test
    public void itShouldRespondWithNotFoundWhenHandicapGroupNotFound() throws EntityNotFoundException {
        when(handicapGroupEntityService.getById(HANDICAP_GROUP_ID)).thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/handicapGroups/" + HANDICAP_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(handicapGroupEntityService).getById(HANDICAP_GROUP_ID);
        verifyNoMoreInteractions(handicapGroupEntityService);
        verifyZeroInteractions(handicapGroupBoundary);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
