package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.CompetitionGroup;

import com.google.common.collect.Sets;

public final class ApiRequestTestUtils {

    private ApiRequestTestUtils() {

    }

    public static AddEventRequest fullAddEvent() {
        return fullAddEvent(
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE
        );
    }

    public static AddEventRequest fullAddEvent(String name, Date date) {
        AddEventRequest request = new AddEventRequest();
        request.setName(name);
        request.setDate(date);
        return request;
    }

    public static AddCompetitionGroupRequest fullAddCompetitionGroup() {
        return fullAddCompetitionGroup(
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE
        );
    }

    public static AddCompetitionGroupRequest fullAddCompetitionGroup(
            String name,
            boolean grouping,
            BigDecimal handicapFactor,
            CompetitionGroup.ResultTimeType resultTimeType
    ) {
        AddCompetitionGroupRequest addCompetitionGroupRequest = new AddCompetitionGroupRequest();
        addCompetitionGroupRequest.setName(name);
        addCompetitionGroupRequest.setGrouping(grouping);
        addCompetitionGroupRequest.setHandicapFactor(handicapFactor);
        addCompetitionGroupRequest.setResultTimeType(resultTimeType.name());
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
                TestConstants.REGISTRATION_LASTNAME
        );
    }

    public static AddRegistrationRequest fullAddRegistration(
            String firstName,
            String lastName
    ) {
        AddRegistrationRequest addRegistrationRequest = new AddRegistrationRequest();
        addRegistrationRequest.setFirstName(firstName);
        addRegistrationRequest.setLastName(lastName);
        return addRegistrationRequest;
    }
}
