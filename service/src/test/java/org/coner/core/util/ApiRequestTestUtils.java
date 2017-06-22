package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.api.request.AddRunRequest;

import com.google.common.collect.Sets;

public final class ApiRequestTestUtils {

    private ApiRequestTestUtils() {

    }

    public static AddEventRequest fullAddEvent() {
        return fullAddEvent(
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                TestConstants.HANDICAP_GROUP_SET_ID,
                TestConstants.COMPETITION_GROUP_SET_ID,
                TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION
        );
    }

    public static AddEventRequest fullAddEvent(
            String name,
            Date date,
            String handicapGroupSetId,
            String competitionGroupSetId,
            int maxRunsPerRegistration
    ) {
        AddEventRequest request = new AddEventRequest();
        request.setName(name);
        request.setDate(date);
        request.setHandicapGroupSetId(handicapGroupSetId);
        request.setCompetitionGroupSetId(competitionGroupSetId);
        request.setMaxRunsPerRegistration(maxRunsPerRegistration);
        return request;
    }

    public static AddCompetitionGroupRequest fullAddCompetitionGroup() {
        return fullAddCompetitionGroup(
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.toString()
        );
    }

    public static AddCompetitionGroupRequest fullAddCompetitionGroup(
            String name,
            boolean grouping,
            BigDecimal handicapFactor,
            String resultTimeType
    ) {
        AddCompetitionGroupRequest addCompetitionGroupRequest = new AddCompetitionGroupRequest();
        addCompetitionGroupRequest.setName(name);
        addCompetitionGroupRequest.setGrouping(grouping);
        addCompetitionGroupRequest.setFactor(handicapFactor);
        addCompetitionGroupRequest.setResultTimeType(resultTimeType);
        return addCompetitionGroupRequest;
    }

    public static AddCompetitionGroupSetRequest fullAddCompetitionGroupSet() {
        return fullAddCompetitionGroupSet(
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID)
        );
    }

    public static AddCompetitionGroupSetRequest fullAddCompetitionGroupSet(
            String competitionGroupSetName,
            Set<String> competitionGroupSetIds
    ) {
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = new AddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName(competitionGroupSetName);
        addCompetitionGroupSetRequest.setCompetitionGroupIds(competitionGroupSetIds);
        return addCompetitionGroupSetRequest;
    }

    public static AddHandicapGroupRequest fullAddHandicapGroup() {
        return fullAddHandicapGroup(
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR
        );
    }

    public static AddHandicapGroupRequest fullAddHandicapGroup(
            String name,
            BigDecimal factor
    ) {
        AddHandicapGroupRequest addHandicapGroupRequest = new AddHandicapGroupRequest();
        addHandicapGroupRequest.setName(name);
        addHandicapGroupRequest.setFactor(factor);
        return addHandicapGroupRequest;
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSet(
            String name,
            Set<String> handicapGroupIds
    ) {
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = new AddHandicapGroupSetRequest();
        addHandicapGroupSetRequest.setName(name);
        addHandicapGroupSetRequest.setHandicapGroupIds(handicapGroupIds);
        return addHandicapGroupSetRequest;
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSet() {
        return fullAddHandicapGroupSet(
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.HANDICAP_GROUP_ID)
        );
    }

    public static AddRegistrationRequest fullAddRegistration() {
        return fullAddRegistration(
                TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME,
                TestConstants.HANDICAP_GROUP_ID
        );
    }

    public static AddRegistrationRequest fullAddRegistration(
            String firstName,
            String lastName,
            String handicapGroupId
    ) {
        AddRegistrationRequest addRegistrationRequest = new AddRegistrationRequest();
        addRegistrationRequest.setFirstName(firstName);
        addRegistrationRequest.setLastName(lastName);
        addRegistrationRequest.setHandicapGroupId(handicapGroupId);
        return addRegistrationRequest;
    }

    public static AddRunRequest fullAddRun() {
        return fullAddRun(
                TestConstants.REGISTRATION_ID,
                TestConstants.RUN_TIMESTAMP,
                TestConstants.RUN_RAW_TIME,
                TestConstants.RUN_CONES,
                TestConstants.RUN_PENALTY,
                TestConstants.RUN_RERUN,
                TestConstants.RUN_COMPETITIVE
        );
    }

    public static AddRunRequest fullAddRun(
            String registrationId,
            Instant timestamp,
            BigDecimal rawTime,
            int cones,
            String penalty,
            boolean rerun,
            boolean competitive
    ) {
        AddRunRequest addRunRequest = new AddRunRequest();
        addRunRequest.setRegistrationId(registrationId);
        addRunRequest.setTimestamp(timestamp);
        addRunRequest.setRawTime(rawTime);
        addRunRequest.setCones(cones);
        addRunRequest.setPenalty(penalty);
        addRunRequest.setRerun(rerun);
        addRunRequest.setCompetitive(competitive);
        return addRunRequest;
    }
}
