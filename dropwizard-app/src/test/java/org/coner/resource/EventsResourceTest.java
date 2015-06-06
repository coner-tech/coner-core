package org.coner.resource;

import org.coner.api.response.*;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Event;
import org.coner.util.JacksonUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EventsResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);
    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventsResource(eventBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        reset(eventBoundary, conerCoreService);

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
        verify(eventBoundary).toApiEntities(domainEvents);
        assertThat(response)
                .isNotNull();
        assertThat(response.getEvents())
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void whenAddValidEventItShouldAddEvent() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        Event requestEventAsDomain = new Event();
        requestEventAsDomain.setId("arbitrary-id-from-service");
        requestEventAsDomain.setName("add this event");
        requestEventAsDomain.setDate(Date.from(ZonedDateTime.parse("2014-12-25T09:15:00-05:00").toInstant()));

        when(eventBoundary.toDomainEntity(requestEvent)).thenReturn(requestEventAsDomain);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);

        verify(conerCoreService).addEvent(requestEventAsDomain);

        assertThat(response)
                .isNotNull();
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void whenAddEventWithUserSuppliedIdItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-with-id.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors())
                .contains("id event.id may only be assigned by the system (was bad-id-in-request)");

        verify(conerCoreService, never()).addEvent(any(Event.class));
    }

    @Test
    public void whenAddEventWithoutNameItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-name.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors()).contains("name may not be empty (was null)");

        verify(conerCoreService, never()).addEvent(any(Event.class));
    }

    @Test
    public void whenAddEventWithoutDateItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-date.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);


        Response response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = response.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
        assertThat(errorsResponse.getErrors()).contains("date may not be null (was null)");

        verify(conerCoreService, never()).addEvent(any(Event.class));
    }
}
