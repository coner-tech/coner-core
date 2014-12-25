package org.axrunner.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.axrunner.api.response.AddEventResponse;
import org.axrunner.api.response.GetEventsResponse;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.AxRunnerCoreService;
import org.axrunner.core.domain.Event;
import org.axrunner.util.JacksonUtil;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;
import static org.mockito.Mockito.*;

/**
 *
 */
public class EventsResourceTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);
    private final AxRunnerCoreService axRunnerCoreService = mock(AxRunnerCoreService.class);

    private ObjectMapper objectMapper;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new EventsResource(eventBoundary, axRunnerCoreService))
            .addProvider(new ConstraintViolationExceptionMapper())
            .build();

    @Before
    public void setup() {
        reset(eventBoundary, axRunnerCoreService);

        objectMapper = Jackson.newObjectMapper();
        JacksonUtil.configureObjectMapper(objectMapper);
    }

    @Test
    public void itShouldGetEvents() {
        List<Event> domainEvents = new ArrayList<>();
        when(axRunnerCoreService.getEvents()).thenReturn(domainEvents);

        GetEventsResponse response = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(GetEventsResponse.class);

        verify(axRunnerCoreService).getEvents();
        verify(eventBoundary).toApiEntities(domainEvents);
        assertThat(response)
                .isNotNull();
        assertThat(response.getEvents())
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void whenAddEventValiditShouldAddEvent() throws Exception {
        org.axrunner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request.json"),
                org.axrunner.api.entity.Event.class
        );
        Entity<org.axrunner.api.entity.Event> requestEntity = Entity.json(requestEvent);
        org.axrunner.api.entity.Event responseEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-response.json"),
                org.axrunner.api.entity.Event.class
        );

        EventBoundary realEventBoundary = new EventBoundary();
        when(eventBoundary.toDomainEntity(requestEvent)).thenReturn(realEventBoundary.toDomainEntity(requestEvent));
        when(eventBoundary.toApiEntity(any(Event.class))).thenReturn(responseEvent);

        AddEventResponse addEventResponse = resources.client()
                .target("/events")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(requestEntity, AddEventResponse.class);

        assertThat(addEventResponse)
                .isNotNull();
        assertThat(addEventResponse.getEvent())
                .isNotNull()
                .isEqualTo(responseEvent);
    }

    @Test
    public void whenAddEventWithUserSuppliedIdItShouldFailValidation() throws Exception {
        org.axrunner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-with-id.json"),
                org.axrunner.api.entity.Event.class
        );
        Entity<org.axrunner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        try {
            AddEventResponse errorResponse = resources.client()
                    .target("/events")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(requestEntity, AddEventResponse.class);
            fail("Should have thrown");
        } catch (ClientErrorException cee) {
            assertThat(cee.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
    }

    @Test
    public void whenAddEventWithoutNameItShouldFailValidation() throws Exception {
        org.axrunner.api.entity.Event requestEvent = objectMapper.readValue(
                FixtureHelpers.fixture("fixtures/api/entity/event_add-request-without-name.json"),
                org.axrunner.api.entity.Event.class
        );
        Entity<org.axrunner.api.entity.Event> requestEntity = Entity.json(requestEvent);

        try {
            AddEventResponse addEventResponse = resources.client()
                    .target("/events")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(requestEntity, AddEventResponse.class);
            fail("should have thrown");
        } catch (ClientErrorException cee) {
            assertThat(cee.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        }
    }
}
