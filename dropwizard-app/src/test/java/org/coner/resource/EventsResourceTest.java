package org.coner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.coner.api.response.AddEventResponse;
import org.coner.api.response.GetEventsResponse;
import org.coner.boundary.EventBoundary;
import org.coner.core.ConerCoreService;
import org.coner.core.domain.Event;
import org.coner.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class EventsResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final ConerCoreService conerCoreService = mock(ConerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventsResource(eventBoundary, conerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

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
        org.coner.api.entity.Event responseEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-response.json"),
                org.coner.api.entity.Event.class
        );
        Event requestEventAsDomain = new Event();
        requestEventAsDomain.setId("arbitrary-id-from-service");
        requestEventAsDomain.setName("add this event");
        requestEventAsDomain.setDate(Date.from(ZonedDateTime.parse("2014-12-25T09:15:00-05:00").toInstant()));

        when(eventBoundary.toDomainEntity(requestEvent)).thenReturn(requestEventAsDomain);
        when(eventBoundary.toApiEntity(any(Event.class))).thenReturn(responseEvent);

        AddEventResponse addEventResponse = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity, AddEventResponse.class);

        verify(conerCoreService).addEvent(requestEventAsDomain);

        assertThat(addEventResponse)
                .isNotNull();
        assertThat(addEventResponse.getEvent())
                .isNotNull()
                .isEqualTo(responseEvent);
    }

    @Test
    public void whenAddEventWithUserSuppliedIdItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-with-id.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        try {
            AddEventResponse errorResponse = resources.client()
                    .target("/events")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(requestEntity, AddEventResponse.class);
            failBecauseExceptionWasNotThrown(ClientErrorException.class);
        } catch (ClientErrorException cee) {
            assertThat(cee.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
        verify(conerCoreService, never()).addEvent(any(Event.class));
    }

    @Test
    public void whenAddEventWithoutNameItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-name.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        try {
            AddEventResponse addEventResponse = resources.client()
                    .target("/events")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(requestEntity, AddEventResponse.class);
            failBecauseExceptionWasNotThrown(ClientErrorException.class);
        } catch (ClientErrorException cee) {
            assertThat(cee.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
        verify(conerCoreService, never()).addEvent(any(Event.class));
    }

    @Test
    public void whenAddEventWithoutDateItShouldFailValidation() throws Exception {
        org.coner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-date.json"),
                org.coner.api.entity.Event.class
        );
        Entity<org.coner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        try {
            AddEventResponse addEventResponse = resources.client()
                    .target("/events")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(requestEntity, AddEventResponse.class);
            failBecauseExceptionWasNotThrown(ClientErrorException.class);
        } catch (ClientErrorException cee) {
            assertThat(cee.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
        verify(conerCoreService, never()).addEvent(any(Event.class));
    }
}
