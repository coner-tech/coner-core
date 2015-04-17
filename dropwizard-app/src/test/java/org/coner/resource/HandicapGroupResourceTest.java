package org.coner.resource;

import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.entity.HandicapGroup;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.ConerCoreService;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.TestConstants;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 */
public class HandicapGroupResourceTest {

    private final HandicapGroupBoundary handicapGroupBoundary = mock(HandicapGroupBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new HandicapGroupResource(handicapGroupBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(handicapGroupBoundary, conerCoreService);
    }

    @Test
    public void itShouldGetHandicapGroup() {
        org.coner.core.domain.HandicapGroup domainHandicapGroup = DomainEntityTestUtils.fullHandicapGroup();
        org.coner.api.entity.HandicapGroup apiHandicapGroup = ApiEntityTestUtils.fullHandicapGroup();

        // sanity check test
        assertThat(domainHandicapGroup.getId()).isSameAs(TestConstants.HANDICAP_GROUP_ID);
        assertThat(apiHandicapGroup.getId()).isSameAs(TestConstants.HANDICAP_GROUP_ID);

        when(conerCoreService.getHandicapGroup(TestConstants.HANDICAP_GROUP_ID)).thenReturn(domainHandicapGroup);
        when(handicapGroupBoundary.toApiEntity(domainHandicapGroup)).thenReturn(apiHandicapGroup);

        Response handicapGroupResponseContainer = resources.client()
                .target("/handicapGroups/" + TestConstants.HANDICAP_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getHandicapGroup(TestConstants.HANDICAP_GROUP_ID);
        verify(handicapGroupBoundary).toApiEntity(domainHandicapGroup);
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        assertThat(handicapGroupResponseContainer).isNotNull();
        assertThat(handicapGroupResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        HandicapGroup getHandicapGroupResponse = handicapGroupResponseContainer.readEntity(HandicapGroup.class);
        assertThat(getHandicapGroupResponse)
                .isNotNull()
                .isEqualTo(apiHandicapGroup);
    }

    @Test
    public void itShouldRespondWithNotFoundWhenHandicapGroupNotFound() {
        when(conerCoreService.getHandicapGroup(TestConstants.HANDICAP_GROUP_ID)).thenReturn(null);

        Response response = resources.client()
                .target("/handicapGroups/" + TestConstants.HANDICAP_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getHandicapGroup(TestConstants.HANDICAP_GROUP_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(handicapGroupBoundary);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }
}
