package org.coner.core.it;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.response.GetEventRegistrationsResponse;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestStandardRequestDelegate;
import org.coner.core.util.IntegrationTestUtils;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import io.dropwizard.jersey.validation.ValidationErrorMessage;

public class RegistrationIntegrationTest extends AbstractIntegrationTest {

    private IntegrationTestStandardRequestDelegate standardRequests;
    private Prerequisites prerequisites;

    @Before
    public void setup() {
        standardRequests = new IntegrationTestStandardRequestDelegate(RULE, client);
        prerequisites = setupPrerequisites();
    }

    @Test
    public void testGetZeroRegistrationsForValidEvent() {
        URI eventRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/registrations")
                .build(prerequisites.eventId);

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
                .build(prerequisites.eventId);

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
                .build(prerequisites.eventId, registrationId0);

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
                .build(prerequisites.eventId);
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

    private Prerequisites setupPrerequisites() {
        prerequisites = new Prerequisites();
        prerequisites.handicapGroupId = standardRequests.addHandicapGroup();
        prerequisites.handicapGroupSetId = standardRequests.addHandicapGroupSet(
                Sets.newHashSet(prerequisites.handicapGroupId)
        );
        prerequisites.eventId = standardRequests.addEvent(prerequisites.handicapGroupSetId);
        return prerequisites;
    }

    private static class Prerequisites {
        private String eventId;
        private String handicapGroupId;
        private String handicapGroupSetId;
    }

}
