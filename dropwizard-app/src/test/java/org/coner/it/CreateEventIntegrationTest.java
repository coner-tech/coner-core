package org.coner.it;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.coner.ConerDropwizardConfiguration;
import org.coner.api.entity.Event;
import org.coner.api.request.AddEventRequest;
import org.coner.api.response.AddEventResponse;
import org.coner.api.response.ErrorsResponse;
import org.coner.api.response.GetEventsResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
public class CreateEventIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<ConerDropwizardConfiguration> RULE = IntegrationTestUtils.buildAppRule();

    private final String name = "name";
    private final Date date = Date.from(ZonedDateTime.parse("2014-12-27T18:28:00-05:00").toInstant());

    @Test
    public void whenCreateEventItShouldPersist() {
        Client client = IntegrationTestUtils.buildClient(RULE);
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

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        AddEventResponse addEventResponse = addEventResponseContainer.readEntity(AddEventResponse.class);
        assertThat(addEventResponse.getEvent()).isNotNull();
        assertThat(addEventResponse.getEvent().getId()).isNotEmpty();
        assertThat(addEventResponse.getEvent().getName()).isEqualTo(name);
        assertThat(addEventResponse.getEvent().getDate()).hasTime(date.getTime());

        Response getEventsResponseContainer = client.target(eventsUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        GetEventsResponse getEventsResponse = getEventsResponseContainer.readEntity(GetEventsResponse.class);
        assertThat(getEventsResponse.getEvents()).hasSize(1);
        Event event = getEventsResponse.getEvents().get(0);
        assertThat(event.getId()).isEqualTo(addEventResponse.getEvent().getId());

        URI getEventByIdUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{id}")
                .build(addEventResponse.getEvent().getId());

        Response getEventByIdResponseContainer = client.target(getEventByIdUri)
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getEventByIdResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        Event getEventByIdResponse = getEventByIdResponseContainer.readEntity(Event.class);
        assertThat(getEventByIdResponse.getId()).isEqualTo(addEventResponse.getEvent().getId());
    }

    @Test
    public void whenCreateEventInvalidItShouldReject() {
        Client client = IntegrationTestUtils.buildClient(RULE);
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
