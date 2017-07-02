package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddRawTimeToFirstRunLackingRequest;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.api.response.GetEventRunsResponse;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestStandardRequestDelegate;
import org.coner.core.util.IntegrationTestUtils;
import org.coner.core.util.TestConstants;
import org.coner.core.util.UnitTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class RunIntegrationTest extends AbstractIntegrationTest {

    private IntegrationTestStandardRequestDelegate standardRequests;
    private Prerequisites prerequisites;

    @Before
    public void setup() {
        standardRequests = new IntegrationTestStandardRequestDelegate(RULE, client);
        prerequisites = setupPrerequisites();
    }

    @Test
    public void itShouldAddRun() {
        URI eventRunsUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs")
                .build(prerequisites.eventId);
        AddRunRequest addRequest = ApiRequestTestUtils.fullAddRun();
        addRequest.setRegistrationId(prerequisites.registrationId);

        Response addResponseContainer = client.target(eventRunsUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRequest));

        assertThat(addResponseContainer.getStatus()).isEqualTo(HttpStatus.CREATED_201);
    }

    @Test
    public void itShouldGetRun() {
        String runId = standardRequests.addRun(prerequisites.eventId, prerequisites.registrationId);
        RunApiEntity expected = ApiEntityTestUtils.fullRun();
        expected.setId(runId);
        expected.setEventId(prerequisites.eventId);
        expected.setRegistrationId(prerequisites.registrationId);

        URI eventRunUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs/{runId}")
                .build(prerequisites.eventId, runId);
        Response getResponseContainer = client.target(eventRunUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        RunApiEntity actual = getResponseContainer.readEntity(RunApiEntity.class);

        assertThat(getResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void itShouldGetAllRunsForEvent() {
        // add expected runs
        List<String> runsForEventIdProperty = Arrays.asList(
                standardRequests.addRun(prerequisites.eventId, prerequisites.registrationId),
                standardRequests.addRun(prerequisites.eventId, prerequisites.registrationId),
                standardRequests.addRun(prerequisites.eventId, prerequisites.registrationId)
        );

        // add a run for a different event
        String anotherEventId = standardRequests.addEvent(
                prerequisites.handicapGroupSetId,
                prerequisites.competitionGroupSetId
        );
        String anotherRegistrationIdAtAnotherEventId = standardRequests.addRegistration(
                anotherEventId, prerequisites.handicapGroupId, prerequisites.competitionGroupId
        );
        standardRequests.addRun(anotherEventId, anotherRegistrationIdAtAnotherEventId);

        URI allRunsForEventUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs")
                .build(prerequisites.eventId);
        Response getResponseContainer = client.target(allRunsForEventUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get();
        GetEventRunsResponse actual = getResponseContainer.readEntity(GetEventRunsResponse.class);

        assertThat(getResponseContainer.getStatus()).isEqualTo(HttpStatus.OK_200);
        assertThat(actual.getEntities())
                .hasSize(runsForEventIdProperty.size())
                .flatExtracting(RunApiEntity::getId).containsExactlyElementsOf(runsForEventIdProperty);
        assertThat(actual.getEntities())
                .flatExtracting(RunApiEntity::getSequence).containsExactly(1, 2, 3);
    }

    @Test
    public void itShouldAddRawTimeToFirstRunInSequenceLackingOne() {
        // add a run without a time
        AddRunRequest addRunRequest = ApiRequestTestUtils.fullAddRun();
        addRunRequest.setRegistrationId(null);
        addRunRequest.setRawTime(null);
        URI addRunUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs")
                .build(prerequisites.eventId);
        Response addRunResponseContainer = client.target(addRunUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRunRequest));
        String runId = UnitTestUtils.getEntityIdFromResponse(addRunResponseContainer);

        // add raw time
        AddRawTimeToFirstRunLackingRequest addRawTimeRequest = new AddRawTimeToFirstRunLackingRequest();
        addRawTimeRequest.setRawTime(TestConstants.RUN_RAW_TIME);
        URI addRawTimeUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs/rawTimes")
                .build(prerequisites.eventId);
        Response addRawTimeResponse = client.target(addRawTimeUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRawTimeRequest));

        // assert
        assertThat(addRawTimeResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
        RunApiEntity addRawTimeRun = addRawTimeResponse.readEntity(RunApiEntity.class);
        assertThat(addRawTimeRun)
                .extracting(RunApiEntity::getId, RunApiEntity::getRawTime)
                .containsExactly(runId, TestConstants.RUN_RAW_TIME);
    }

    @Test
    public void itShouldCreateRunIfAddRawTimeToFirstRunInSequenceFindsNonePriorLackingRawTime() {
        // add a full run
        AddRunRequest addRunRequest = ApiRequestTestUtils.fullAddRun();
        addRunRequest.setRegistrationId(prerequisites.registrationId);
        URI addRunUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs")
                .build(prerequisites.eventId);
        Response addRunResponseContainer = client.target(addRunUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRunRequest));
        String originalFullRunId = UnitTestUtils.getEntityIdFromResponse(addRunResponseContainer);

        // add raw time (perhaps there was a false start/finish trip, or car launched unknown to T&S workers)
        AddRawTimeToFirstRunLackingRequest addRawTimeRequest = new AddRawTimeToFirstRunLackingRequest();
        addRawTimeRequest.setRawTime(TestConstants.RUN_RAW_TIME);
        URI addRawTimeUri = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/runs/rawTimes")
                .build(prerequisites.eventId);
        Response addRawTimeResponse = client.target(addRawTimeUri)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.json(addRawTimeRequest));

        // assert
        assertThat(addRawTimeResponse.getStatus()).isEqualTo(HttpStatus.CREATED_201);
        RunApiEntity addRawTimeRun = addRawTimeResponse.readEntity(RunApiEntity.class);
        assertThat(addRawTimeRun.getId())
                .isEqualTo(UnitTestUtils.getEntityIdFromResponse(addRawTimeResponse))
                .isNotEqualTo(originalFullRunId);
        assertThat(addRawTimeRun.getRawTime()).isEqualTo(TestConstants.RUN_RAW_TIME);
    }

    private Prerequisites setupPrerequisites() {
        Prerequisites prerequisites = new Prerequisites();
        prerequisites.handicapGroupId = standardRequests.addHandicapGroup();
        prerequisites.handicapGroupSetId = standardRequests.addHandicapGroupSet(
                Sets.newHashSet(prerequisites.handicapGroupId)
        );
        prerequisites.competitionGroupId = standardRequests.addCompetitionGroup();
        prerequisites.competitionGroupSetId = standardRequests.addCompetitionGroupSet(
                Sets.newHashSet(prerequisites.competitionGroupId)
        );
        prerequisites.eventId = standardRequests.addEvent(
                prerequisites.handicapGroupSetId,
                prerequisites.competitionGroupSetId
        );
        prerequisites.registrationId = standardRequests.addRegistration(
                prerequisites.eventId,
                prerequisites.handicapGroupId,
                prerequisites.competitionGroupId
        );
        return prerequisites;
    }

    private static class Prerequisites {
        private String eventId;
        private String registrationId;
        private String handicapGroupId;
        private String handicapGroupSetId;
        private String competitionGroupId;
        private String competitionGroupSetId;
    }
}
