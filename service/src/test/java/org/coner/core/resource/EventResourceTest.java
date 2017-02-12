package org.coner.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coner.core.util.TestConstants.EVENT_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.service.EventEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.mapper.EventMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.dropwizard.testing.junit.ResourceTestRule;

public class EventResourceTest {

    private final EventMapper eventMapper = mock(EventMapper.class);
    private final EventEntityService eventEntityService = mock(EventEntityService.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventResource(eventMapper, eventEntityService))
            .addResource(new DomainServiceExceptionMapper())
            .build();


    @Before
    public void setup() {
        reset(eventMapper, eventEntityService);
    }

    @Test
    public void itShouldGetEvent() throws EntityNotFoundException {
        Event domainEvent = DomainEntityTestUtils.fullDomainEvent();
        EventApiEntity apiEvent = ApiEntityTestUtils.fullApiEvent();

        // sanity check test
        assertThat(domainEvent.getId()).isSameAs(EVENT_ID);
        assertThat(apiEvent.getId()).isSameAs(EVENT_ID);

        when(eventEntityService.getById(EVENT_ID)).thenReturn(domainEvent);
        when(eventMapper.toApiEntity(domainEvent)).thenReturn(apiEvent);

        Response getEventResponseContainer = resources.client()
                .target("/events/" + EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventEntityService).getById(EVENT_ID);
        verify(eventMapper).toApiEntity(domainEvent);
        verifyNoMoreInteractions(eventEntityService, eventMapper);

        assertThat(getEventResponseContainer).isNotNull();
        assertThat(getEventResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        EventApiEntity getEventResponse = getEventResponseContainer.readEntity(EventApiEntity.class);
        assertThat(getEventResponse)
                .isNotNull()
                .isEqualTo(apiEvent);
    }

    @Test
    public void itShouldRespondWithNotFoundErrorWhenEventIdInvalid() throws EntityNotFoundException {
        when(eventEntityService.getById(EVENT_ID)).thenThrow(EntityNotFoundException.class);

        Response response = resources.client()
                .target("/events/" + EVENT_ID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        verify(eventEntityService).getById(EVENT_ID);
        verifyNoMoreInteractions(eventEntityService);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
