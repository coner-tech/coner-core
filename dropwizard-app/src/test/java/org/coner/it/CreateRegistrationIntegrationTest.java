package org.coner.it;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.coner.ConerDropwizardConfiguration;
import org.coner.api.entity.Registration;
import org.coner.api.request.AddEventRequest;
import org.coner.api.request.AddRegistrationRequest;
import org.coner.api.response.ErrorsResponse;
import org.coner.api.response.GetEventRegistrationsResponse;
import org.coner.util.TestConstants;
import org.coner.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class CreateRegistrationIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<ConerDropwizardConfiguration> RULE = IntegrationTestUtils.buildAppRule();

    private String eventId;
    private final String eventName = TestConstants.EVENT_NAME;
    private final Date eventDate = TestConstants.EVENT_DATE;

    @Before
    public void setup() {
        // Create Event to which a Registration will be added
        Client client = IntegrationTestUtils.buildClient(RULE);
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = new AddEventRequest();
        addEventRequest.setName(eventName);
        addEventRequest.setDate(eventDate);
        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        eventId = UnitTestUtils.getEntityIdFromResponse(addEventResponseContainer);
    }

    @Test
    public void testGetZeroRegistrationsForValidEvent() {
        Client client = IntegrationTestUtils.buildClient(RULE);
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);

        Response getRegistrationsResponseContainer = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        GetEventRegistrationsResponse getEventRegistrationsResponse = getRegistrationsResponseContainer
                .readEntity(GetEventRegistrationsResponse.class);
        assertThat(getEventRegistrationsResponse).isNotNull();
        assertThat(getEventRegistrationsResponse.getRegistrations().size()).isZero();
    }

    @Test
    public void whenCreateRegistrationItShouldPersist() {
        Client client = IntegrationTestUtils.buildClient(RULE);
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);

        // Post first Registration
        AddRegistrationRequest addRegistrationRequest0 = new AddRegistrationRequest();
        addRegistrationRequest0.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        addRegistrationRequest0.setLastName(TestConstants.REGISTRATION_LASTNAME);

        Response addRegistrationResponse0 = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest0));

        assertThat(addRegistrationResponse0.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String registrationId0 = UnitTestUtils.getEntityIdFromResponse(addRegistrationResponse0);

        // Post second Registration
        AddRegistrationRequest addRegistrationRequest1 = new AddRegistrationRequest();
        addRegistrationRequest1.setFirstName(TestConstants.REGISTRATION_FIRSTNAME + "-2");
        addRegistrationRequest1.setLastName(TestConstants.REGISTRATION_LASTNAME + "-2");

        Response addRegistrationResponse1 = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest1));

        assertThat(addRegistrationResponse1.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String registrationId1 = UnitTestUtils.getEntityIdFromResponse(addRegistrationResponse1);

        // work around Jersey client hanging when same client from above reused
        Client client2 = IntegrationTestUtils.buildClient(RULE);

        // Get Registrations for Event
        Response getRegistrationsResponseContainer = client2.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        GetEventRegistrationsResponse getEventRegistrationsResponse = getRegistrationsResponseContainer
                .readEntity(GetEventRegistrationsResponse.class);
        assertThat(getEventRegistrationsResponse).isNotNull();
        List<Registration> registrationList = getEventRegistrationsResponse.getRegistrations();
        assertThat(registrationList.size()).isEqualTo(2);
        assertThat(registrationList.get(0).getId())
                .isNotEqualTo(registrationList.get(1).getId())
                .isEqualTo(registrationId0);
        assertThat(registrationList.get(1).getId())
                .isEqualTo(registrationId1);
        assertThat(registrationList.get(0).getEvent().getId())
                .isEqualTo(registrationList.get(1).getEvent().getId());

        URI eventRegistrationUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations/{registrationId}")
                .build(eventId, registrationId0);

        // Get Registration
        Response getRegistrationResponse = client2.target(eventRegistrationUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationResponse.getStatus()).isEqualTo(HttpStatus.OK_200);

        Registration registration = getRegistrationResponse
                .readEntity(Registration.class);
        assertThat(registration).isNotNull();
        assertThat(registration.getEvent().getId()).isEqualTo(eventId);
        assertThat(registration.getId()).isEqualTo(registrationId0);

    }

    @Test
    public void whenCreateInvalidRegistrationItShouldReject() {
        Client client = IntegrationTestUtils.buildClient(RULE);
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);
        AddRegistrationRequest addRegistrationRequest = new AddRegistrationRequest();

        Response addRegistrationResponseContainer = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest));

        assertThat(addRegistrationResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ErrorsResponse errorsResponse = addRegistrationResponseContainer.readEntity(ErrorsResponse.class);
        assertThat(errorsResponse.getErrors()).isNotEmpty();
    }

    @Test
    public void whenInvalidEventItShouldReject() {
        Client client = IntegrationTestUtils.buildClient(RULE);
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build("987654321");

        // Post Registration
        AddRegistrationRequest addRegistrationRequest = new AddRegistrationRequest();
        addRegistrationRequest.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        addRegistrationRequest.setLastName(TestConstants.REGISTRATION_LASTNAME);

        Response addRegistrationResponse = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest));

        assertThat(addRegistrationResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
