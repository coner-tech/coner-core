package org.coner.core.it;

import static org.coner.core.util.TestConstants.EVENT_DATE;
import static org.coner.core.util.TestConstants.EVENT_NAME;
import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.response.GetEventRegistrationsResponse;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestUtils;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import io.dropwizard.jersey.validation.ValidationErrorMessage;

public class RegistrationIntegrationTest extends AbstractIntegrationTest {

    private String eventId;

    @Before
    public void setup() {
        // Create Event to which a Registration will be added
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events")
                .build();
        AddEventRequest addEventRequest = new AddEventRequest();
        addEventRequest.setName(EVENT_NAME);
        addEventRequest.setDate(EVENT_DATE);
        Response addEventResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addEventRequest));

        assertThat(addEventResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        eventId = UnitTestUtils.getEntityIdFromResponse(addEventResponseContainer);
    }

    @Test
    public void testGetZeroRegistrationsForValidEvent() {
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);

        Response getRegistrationsResponseContainer = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        GetEventRegistrationsResponse response = getRegistrationsResponseContainer.readEntity(
                GetEventRegistrationsResponse.class
        );
        assertThat(response.getEntities()).isEmpty();
    }

    @Test
    public void whenCreateRegistrationsItShouldPersist() {
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);

        // Post Registration #0
        AddRegistrationRequest addRegistrationRequest0 = ApiRequestTestUtils.fullAddRegistration();
        addRegistrationRequest0.setFirstName(addRegistrationRequest0.getFirstName() + "-0");
        addRegistrationRequest0.setLastName(addRegistrationRequest0.getLastName() + "-0");

        Response addRegistrationResponse0 = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest0));
        assertThat(addRegistrationResponse0.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String registrationId0 = UnitTestUtils.getEntityIdFromResponse(addRegistrationResponse0);

        // Post Registration #1
        AddRegistrationRequest addRegistrationRequest1 = new AddRegistrationRequest();
        addRegistrationRequest1.setFirstName(addRegistrationRequest1.getFirstName() + "-1");
        addRegistrationRequest1.setLastName(addRegistrationRequest1.getLastName() + "-1");

        Response addRegistrationResponse1 = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest1));

        assertThat(addRegistrationResponse1.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        final String registrationId1 = UnitTestUtils.getEntityIdFromResponse(addRegistrationResponse1);

        // Get Registrations for Event
        Response getRegistrationsResponseContainer = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationsResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);

        GetEventRegistrationsResponse getEventRegistrationsResponse = getRegistrationsResponseContainer
                .readEntity(GetEventRegistrationsResponse.class);
        assertThat(getEventRegistrationsResponse).isNotNull();
        List<RegistrationApiEntity> registrationList = getEventRegistrationsResponse.getEntities();
        assertThat(registrationList.size()).isEqualTo(2);
        assertThat(registrationList.get(0).getId())
                .isNotEqualTo(registrationList.get(1).getId())
                .isEqualTo(registrationId0);
        assertThat(registrationList.get(1).getId())
                .isEqualTo(registrationId1);

        URI eventRegistrationUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations/{registrationId}")
                .build(eventId, registrationId0);

        // Get Registration
        Response getRegistrationResponse = client.target(eventRegistrationUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(getRegistrationResponse.getStatus()).isEqualTo(HttpStatus.OK_200);

        RegistrationApiEntity registration = getRegistrationResponse
                .readEntity(RegistrationApiEntity.class);
        assertThat(registration).isNotNull();
        assertThat(registration.getId()).isEqualTo(registrationId0);
    }

    @Test
    public void whenCreateInvalidRegistrationItShouldReject() {
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(eventId);
        AddRegistrationRequest addRegistrationRequest = ApiRequestTestUtils.fullAddRegistration();
        addRegistrationRequest.setFirstName(null);
        addRegistrationRequest.setLastName(null);

        Response addRegistrationResponseContainer = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest));

        assertThat(addRegistrationResponseContainer.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY_422);
        ValidationErrorMessage validationErrorMessage = addRegistrationResponseContainer.readEntity(
                ValidationErrorMessage.class
        );
        assertThat(validationErrorMessage.getErrors()).isNotEmpty();
    }

    @Test
    public void whenInvalidEventItShouldRespondNotFound() {
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build("987654321");

        // Post Registration
        AddRegistrationRequest addRegistrationRequest = ApiRequestTestUtils.fullAddRegistration();

        Response addRegistrationResponse = client.target(eventRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRegistrationRequest));

        assertThat(addRegistrationResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
    }

}
