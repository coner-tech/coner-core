package org.coner.core.util;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.assertj.core.api.Assertions;
import org.coner.core.ConerCoreConfiguration;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.request.AddRunRequest;
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

    public String addEvent(String handicapGroupSetId, String competitionGroupSetId) {
        URI eventsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/events")
                .build();
        AddEventRequest addRequest = ApiRequestTestUtils.fullAddEvent();
        addRequest.setHandicapGroupSetId(handicapGroupSetId);
        addRequest.setCompetitionGroupSetId(competitionGroupSetId);
        Response addResponseContainer = client.target(eventsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addHandicapGroup() {
        return addHandicapGroup(ApiRequestTestUtils.fullAddHandicapGroup());
    }

    public String addHandicapGroup(AddHandicapGroupRequest addHandicapGroupRequest) {
        URI uri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/handicapGroups")
                .build();
        Response addResponseContainer = client.target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addHandicapGroupSet(Set<String> handicapGroupIds) {
        URI uri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/handicapGroups/sets")
                .build();
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = ApiRequestTestUtils.fullAddHandicapGroupSet();
        addHandicapGroupSetRequest.setHandicapGroupIds(handicapGroupIds);
        Response addResponseContainer = client.target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addHandicapGroupSetRequest));
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

    public String addCompetitionGroupSet(Set<String> competitionGroupIds) {
        URI uri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/competitionGroups/sets")
                .build();
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet();
        addCompetitionGroupSetRequest.setCompetitionGroupIds(competitionGroupIds);
        Response addResponseContainer = client.target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addCompetitionGroupSetRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addRegistration(String eventId, String handicapGroupId) {
        URI eventsRegistrationsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/events/{eventId}/registrations")
                .build(eventId);
        AddRegistrationRequest addRequest = ApiRequestTestUtils.fullAddRegistration();
        addRequest.setHandicapGroupId(handicapGroupId);
        Response addResponseContainer = client.target(eventsRegistrationsUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(addRequest));
        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }

    public String addRun(String eventId, String registrationId) {
        URI eventRunsUri = IntegrationTestUtils.jerseyUriBuilderForApp(rule)
                .path("/events/{eventId}/runs")
                .build(eventId);
        AddRunRequest addRequest = ApiRequestTestUtils.fullAddRun();
        addRequest.setRegistrationId(registrationId);
        Response addResponseContainer = client.target(eventRunsUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRequest));
        Assertions.assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        return UnitTestUtils.getEntityIdFromResponse(addResponseContainer);
    }
}
