package org.coner.resource;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.request.AddEventRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.api.response.GetEventsResponse;
import org.coner.boundary.EventApiAddPayloadBoundary;
import org.coner.boundary.EventApiDomainBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class EventsResourceTest {

    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);
    private final EventApiDomainBoundary apiDomainBoundary = mock(EventApiDomainBoundary.class);
    private final EventApiAddPayloadBoundary addPayloadBoundary = mock(EventApiAddPayloadBoundary.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventsResource(conerCoreService, apiDomainBoundary, addPayloadBoundary))
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(apiDomainBoundary, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetEvents() {
        List<Event> domainEvents = new ArrayList<>();
        when(conerCoreService.getEvents()).thenReturn(domainEvents);

        GetEventsResponse response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventsResponse.class);

        verify(conerCoreService).getEvents();
        verify(apiDomainBoundary).toLocalEntities(domainEvents);
        assertThat(response)
                .isNotNull();
        assertThat(response.getEvents())
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void whenAddValidEventItShouldAddEvent() throws Exception {
        AddEventRequest requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request.json"),
                AddEventRequest.class
        );
        Entity<AddEventRequest> requestEntity = Entity.json(requestEvent);
        EventAddPayload addPayload = mock(EventAddPayload.class);
        when(addPayloadBoundary.toRemoteEntity(requestEvent)).thenReturn(addPayload);
        Event domainEntity = mock(Event.class);
        when(conerCoreService.addEvent(addPayload)).thenReturn(domainEntity);
        EventApiEntity apiEntity = ApiEntityTestUtils.fullApiEvent();
        when(apiDomainBoundary.toLocalEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addEvent(addPayload);
        verifyNoMoreInteractions(conerCoreService);
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void whenAddEventWithoutNameItShouldFailValidation() throws Exception {
        AddEventRequest requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-name.json"),
                AddEventRequest.class
        );
        Entity<AddEventRequest> requestEntity = Entity.json(requestEvent);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors()).contains("name may not be empty");
        verifyZeroInteractions(conerCoreService);
    }

    @Test
    public void whenAddEventWithoutDateItShouldFailValidation() throws Exception {
        AddEventRequest requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-date.json"),
                AddEventRequest.class
        );
        Entity<AddEventRequest> requestEntity = Entity.json(requestEvent);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors()).contains("date may not be null");

        verifyZeroInteractions(conerCoreService);
    }
}
