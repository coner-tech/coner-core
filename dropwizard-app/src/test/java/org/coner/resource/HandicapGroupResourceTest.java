package org.coner.resource;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.boundary.HandicapGroupApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.HandicapGroup;
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

public class HandicapGroupResourceTest {

    private final HandicapGroupApiDomainBoundary handicapGroupBoundary = mock(HandicapGroupApiDomainBoundary.class);
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
        HandicapGroup domainHandicapGroup = DomainEntityTestUtils.fullHandicapGroup();
        HandicapGroupApiEntity handicapGroupApiEntity = ApiEntityTestUtils.fullHandicapGroup();

        // sanity check test
        assertThat(domainHandicapGroup.getId()).isSameAs(TestConstants.HANDICAP_GROUP_ID);
        assertThat(handicapGroupApiEntity.getId()).isSameAs(TestConstants.HANDICAP_GROUP_ID);

        when(conerCoreService.getHandicapGroup(TestConstants.HANDICAP_GROUP_ID)).thenReturn(domainHandicapGroup);
        when(handicapGroupBoundary.toLocalEntity(domainHandicapGroup)).thenReturn(handicapGroupApiEntity);

        Response responseContainer = resources.client()
                .target("/handicapGroups/" + TestConstants.HANDICAP_GROUP_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getHandicapGroup(TestConstants.HANDICAP_GROUP_ID);
        verify(handicapGroupBoundary).toLocalEntity(domainHandicapGroup);
        verifyNoMoreInteractions(conerCoreService, handicapGroupBoundary);

        assertThat(responseContainer).isNotNull();
        assertThat(responseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        HandicapGroupApiEntity getHandicapGroupResponse = responseContainer.readEntity(HandicapGroupApiEntity.class);
        assertThat(getHandicapGroupResponse)
                .isNotNull()
                .isEqualTo(handicapGroupApiEntity);
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
