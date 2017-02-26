package org.coner.core.resource;

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

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.response.GetEventsResponse;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.service.EventEntityService;
import org.coner.core.mapper.EventMapper;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;

public class EventsResourceTest {

    private final EventEntityService conerCoreService = mock(EventEntityService.class);
    private final EventMapper eventMapper = mock(EventMapper.class);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventsResource(conerCoreService, eventMapper))
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(eventMapper, conerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetEvents() {
        List<Event> domainEvents = new ArrayList<>();
        when(conerCoreService.getAll()).thenReturn(domainEvents);

        GetEventsResponse response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventsResponse.class);

        verify(conerCoreService).getAll();
        verify(eventMapper).toApiEntityList(domainEvents);
        assertThat(response)
                .isNotNull();
        assertThat(response.getEntities())
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
        when(eventMapper.toDomainAddPayload(requestEvent)).thenReturn(addPayload);
        Event domainEntity = mock(Event.class);
        when(conerCoreService.add(addPayload)).thenReturn(domainEntity);
        EventApiEntity apiEntity = ApiEntityTestUtils.fullEvent();
        when(eventMapper.toApiEntity(domainEntity)).thenReturn(apiEntity);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).add(addPayload);
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
        ValidationErrorMessage validationErrorMessage = response.readEntity(ValidationErrorMessage.class);
        assertThat(validationErrorMessage.getErrors())
                .isNotEmpty()
                .contains("name may not be empty");
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
        ValidationErrorMessage validationErrorMessage = response.readEntity(ValidationErrorMessage.class);
        assertThat(validationErrorMessage.getErrors())
                .isNotEmpty()
                .contains("date may not be null");

        verifyZeroInteractions(conerCoreService);
    }
}
