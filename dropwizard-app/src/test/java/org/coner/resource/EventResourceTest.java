package org.coner.resource;

import org.coner.api.entity.EventApiEntity;
import org.coner.boundary.EventApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Event;
import org.coner.util.*;

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

public class EventResourceTest {

    private final EventApiDomainBoundary eventBoundary = mock(EventApiDomainBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventResource(eventBoundary, conerCoreService))
            .build();


    @Before
    public void setup() {
        reset(eventBoundary, conerCoreService);
    }

    @Test
    public void itShouldGetEvent() {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        EventApiEntity apiEvent = ApiEntityTestUtils.fullApiEvent();

        // sanity check test
        assertThat(domainEvent.getId()).isSameAs(TestConstants.EVENT_ID);
        assertThat(apiEvent.getId()).isSameAs(TestConstants.EVENT_ID);

        when(conerCoreService.getEvent(TestConstants.EVENT_ID)).thenReturn(domainEvent);
        when(eventBoundary.toLocalEntity(domainEvent)).thenReturn(apiEvent);

        Response getEventResponseContainer = resources.client()
                .target("/events/" + TestConstants.EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(TestConstants.EVENT_ID);
        verify(eventBoundary).toLocalEntity(domainEvent);
        verifyNoMoreInteractions(conerCoreService, eventBoundary);

        assertThat(getEventResponseContainer).isNotNull();
        assertThat(getEventResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        EventApiEntity getEventResponse = getEventResponseContainer.readEntity(EventApiEntity.class);
        assertThat(getEventResponse)
                .isNotNull()
                .isEqualTo(apiEvent);
    }

    @Test
    public void itShouldRespondWithNotFoundErrorWhenEventIdInvalid() {
        when(conerCoreService.getEvent(TestConstants.EVENT_ID)).thenReturn(null);

        Response response = resources.client()
                .target("/events/" + TestConstants.EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(TestConstants.EVENT_ID);
        verifyNoMoreInteractions(conerCoreService);
        verifyZeroInteractions(eventBoundary);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
