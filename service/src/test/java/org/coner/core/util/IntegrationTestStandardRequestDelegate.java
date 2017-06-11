package org.coner.core.util;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.ConerCoreConfiguration;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddRegistrationRequest;
import org.eclipse.jetty.http.HttpStatus;

import io.dropwizard.testing.junit.DropwizardAppRule;

public final class IntegrationTestStandardRequestDelegate {

    private final DropwizardAppRule<ConerCoreConfiguration> rule;
    private final Client client;

    public IntegrationTestStandardRequestDelegate(
            DropwizardAppRule<ConerCoreConfiguration> rule,
            Client client
    ) {
        this.rule = rule;
        this.client = client;
    }

    public String addEvent() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/events")
                .build();
        AddEventRequest addRequest = ApiRequestTestUtils.fullAddEvent();
        Response addResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addHandicapGroup() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/handicapGroups")
                .build();
        AddHandicapGroupRequest addRequest = ApiRequestTestUtils.fullAddHandicapGroup();
        Response addResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addCompetitionGroup() {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/competitionGroups")
                .build();
        AddCompetitionGroupRequest addRequest = ApiRequestTestUtils.fullAddCompetitionGroup();
        Response addResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addRegistration(String eventId) {
        URI eventsRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/events/{eventId}/registrations")
                .build(eventId);
        AddRegistrationRequest addRequest = ApiRequestTestUtils.fullAddRegistration();
        Response addResponseContainer = client.target(eventsRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }
}
