package org.coner.core.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.assertj.core.util.Lists;
import org.coner.core.api.entity.ScoredRunApiEntity;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.api.response.GetEventResultsRegistrationResponse;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.IntegrationTestStandardRequestDelegate;
import org.coner.core.util.IntegrationTestUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.Before;
import org.junit.Test;

public class ResultsIntegrationTest extends AbstractIntegrationTest {

    private IntegrationTestStandardRequestDelegate standardRequests;
    private Prerequisites prerequisites;
    private JerseyUriBuilder eventResultsRegistrationUriBuilder;

    @Before
    public void setup() {
        standardRequests = new IntegrationTestStandardRequestDelegate(RULE, client);
        prerequisites = setupPrerequisites();
        eventResultsRegistrationUriBuilder = IntegrationTestUtils.jerseyUriBuilderForApp(RULE)
                .path("/events/{eventId}/results/registration/{registrationId}");
    }

    @Test
    public void itShouldScoreForSs() {
        String[] ssRuns = new String[3];
        ScoredRunApiEntity[] expectedScoredRuns = new ScoredRunApiEntity[3];
        String ssRegistrationId = prerequisites.registrationIds[0];

        // add ss run 1
        AddRunRequest addRunRequest = ApiRequestTestUtils.fullAddRun();
        addRunRequest.setRegistrationId(ssRegistrationId);
        addRunRequest.setRawTime(BigDecimal.valueOf(45678L, 3));
        addRunRequest.setCones(0);
        ssRuns[0] = standardRequests.addRun(prerequisites.eventId, addRunRequest);
        expectedScoredRuns[0] = new ScoredRunApiEntity();
        expectedScoredRuns[0].setRunId(ssRuns[0]);
        expectedScoredRuns[0].setRawTimeScored(BigDecimal.valueOf(45678L, 3));
        expectedScoredRuns[0].setHandicapTimeScored(BigDecimal.valueOf(37730L, 3));

        // add ss run 2
        addRunRequest = ApiRequestTestUtils.fullAddRun();
        addRunRequest.setRegistrationId(ssRegistrationId);
        addRunRequest.setRawTime(BigDecimal.valueOf(44321L, 3));
        addRunRequest.setCones(0);
        ssRuns[1] = standardRequests.addRun(prerequisites.eventId, addRunRequest);
        expectedScoredRuns[1] = new ScoredRunApiEntity();
        expectedScoredRuns[1].setRunId(ssRuns[1]);
        expectedScoredRuns[1].setRawTimeScored(BigDecimal.valueOf(44321L, 3));
        expectedScoredRuns[1].setHandicapTimeScored(BigDecimal.valueOf(36609L, 3));

        // add ss run 3
        addRunRequest = ApiRequestTestUtils.fullAddRun();
        addRunRequest.setRegistrationId(ssRegistrationId);
        addRunRequest.setRawTime(BigDecimal.valueOf(44123L, 3));
        addRunRequest.setCones(1);
        ssRuns[2] = standardRequests.addRun(prerequisites.eventId, addRunRequest);
        expectedScoredRuns[2] = new ScoredRunApiEntity();
        expectedScoredRuns[2].setRunId(ssRuns[2]);
        expectedScoredRuns[2].setRawTimeScored(BigDecimal.valueOf(46123L, 3));
        expectedScoredRuns[2].setHandicapTimeScored(BigDecimal.valueOf(38446L, 3));
        GetEventResultsRegistrationResponse expectedResponseBody = new GetEventResultsRegistrationResponse();
        expectedResponseBody.setRegistrationId(prerequisites.registrationIds[0]);
        expectedResponseBody.setScore(expectedScoredRuns[1]);
        expectedResponseBody.setScoredRuns(Lists.newArrayList(expectedScoredRuns));

        URI eventResultsRegistrationUri = eventResultsRegistrationUriBuilder.build(
                prerequisites.eventId, ssRegistrationId
        );
        Response actualResponse = client.target(eventResultsRegistrationUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertThat(actualResponse.getStatus()).isEqualTo(HttpStatus.OK_200);
        GetEventResultsRegistrationResponse actualResponseBody = actualResponse.readEntity(
                GetEventResultsRegistrationResponse.class
        );
        assertThat(actualResponseBody).isEqualTo(expectedResponseBody);
    }

    private Prerequisites setupPrerequisites() {
        Prerequisites prerequisites = new Prerequisites();
        prerequisites.handicapGroupIds = new String[2];
        prerequisites.handicapGroupIds[0] = standardRequests.addHandicapGroup(
                ApiRequestTestUtils.fullAddHandicapGroup("SS", BigDecimal.valueOf(826, 3))
        );
        prerequisites.handicapGroupIds[1] = standardRequests.addHandicapGroup(
                ApiRequestTestUtils.fullAddHandicapGroup("HS", BigDecimal.valueOf(786, 3))
        );
        prerequisites.competitionGroupIds = new String[2];
        prerequisites.competitionGroupIds[0] = standardRequests.addCompetitionGroup(
                ApiRequestTestUtils.fullAddCompetitionGroup(
                        "Open",
                        false,
                        BigDecimal.ONE,
                        CompetitionGroup.ResultTimeType.RAW.name()
                )
        );
        prerequisites.competitionGroupIds[1] = standardRequests.addCompetitionGroup(
                ApiRequestTestUtils.fullAddCompetitionGroup(
                        "Novice",
                        true,
                        BigDecimal.ONE,
                        CompetitionGroup.ResultTimeType.HANDICAP.name()
                )
        );
        prerequisites.handicapGroupSetId = standardRequests.addHandicapGroupSet(
                prerequisites.handicapGroupIds[0], prerequisites.handicapGroupIds[1]
        );
        prerequisites.competitionGroupSetId = standardRequests.addCompetitionGroupSet(
                prerequisites.competitionGroupIds[0], prerequisites.competitionGroupIds[1]
        );
        prerequisites.eventId = standardRequests.addEvent(
                prerequisites.handicapGroupSetId,
                prerequisites.competitionGroupSetId
        );
        prerequisites.registrationIds = new String[2];
        prerequisites.registrationIds[0] = standardRequests.addRegistration(
                prerequisites.eventId,
                prerequisites.handicapGroupIds[0],
                prerequisites.competitionGroupIds[0]
        );
        prerequisites.registrationIds[1] = standardRequests.addRegistration(
                prerequisites.eventId,
                prerequisites.handicapGroupIds[1],
                prerequisites.competitionGroupIds[1]
        );
        return prerequisites;
    }

    private static class Prerequisites {
        private String eventId;
        private String[] registrationIds;
        private String[] handicapGroupIds;
        private String handicapGroupSetId;
        private String[] competitionGroupIds;
        private String competitionGroupSetId;
    }
}
