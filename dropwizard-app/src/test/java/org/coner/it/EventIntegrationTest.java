package org.coner.it;

import org.coner.api.entity.EventApiEntity;
import org.coner.api.request.AddEventRequest;
import org.coner.api.response.*;
import org.coner.util.UnitTestUtils;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EventIntegrationTest extends AbstractIntegrationTest {

    private final String name = "name";
    private final Date date = Date.from(ZonedDateTime.parse("2014-12-27T18:28:00-05:00").toInstant());

    @Test
    public void whenCreateEventItShouldPersist() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = new AddEventRequest();
        addEventRequest.setName(name);
        addEventRequest.setDate(date);
        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String eventId = UnitTestUtils.getEntityIdFromResponse(addEventResponseContainer);

        Response getEventsResponseContainer = client.target(eventsUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        GetEventsResponse getEventsResponse = getEventsResponseContainer.readEntity(GetEventsResponse.class);
        assertThat(getEventsResponse.getEvents()).hasSize(1);
        EventApiEntity event = getEventsResponse.getEvents().get(0);
        assertThat(event.getId()).isEqualTo(eventId);

        URI getEventByIdUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{id}")
                .build(eventId);

        Response getEventByIdResponseContainer = client.target(getEventByIdUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventByIdResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        EventApiEntity getEventByIdResponse = getEventByIdResponseContainer.readEntity(EventApiEntity.class);
        assertThat(getEventByIdResponse.getId()).isEqualTo(eventId);
    }

    @Test
    public void whenCreateEventInvalidItShouldReject() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = new AddEventRequest();

        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = addEventResponseContainer.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
    }

}
