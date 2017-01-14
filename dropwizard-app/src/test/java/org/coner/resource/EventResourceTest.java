package org.coner.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.util.TestConstants.EVENT_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.api.entity.EventApiEntity;
import org.coner.boundary.EventApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Event;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;

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
    public void itShouldGetEvent() throws EntityNotFoundException {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        EventApiEntity apiEvent = ApiEntityTestUtils.fullApiEvent();

        // sanity check test
        assertThat(domainEvent.getId()).isSameAs(EVENT_ID);
        assertThat(apiEvent.getId()).isSameAs(EVENT_ID);

        when(conerCoreService.getEvent(EVENT_ID)).thenReturn(domainEvent);
        when(eventBoundary.toLocalEntity(domainEvent)).thenReturn(apiEvent);

        Response getEventResponseContainer = resources.client()
                .target("/events/" + EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(EVENT_ID);
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
    public void itShouldRespondWithNotFoundErrorWhenEventIdInvalid() throws EntityNotFoundException {
        when(conerCoreService.getEvent(EVENT_ID)).thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/events/" + EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(conerCoreService).getEvent(EVENT_ID);
        verifyNoMoreInteractions(conerCoreService);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
