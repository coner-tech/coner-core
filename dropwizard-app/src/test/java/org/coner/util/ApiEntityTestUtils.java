package org.coner.util;

import org.coner.api.entity.*;
import org.coner.api.request.AddHandicapGroupSetRequest;

import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.util.*;

public final class ApiEntityTestUtils {

    private ApiEntityTestUtils() {
    }

    public static Event fullApiEvent() {
        return fullApiEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullApiEvent(String eventId, String eventName, Date eventDate) {
        Event apiEvent = new Event();
        apiEvent.setId(eventId);
        apiEvent.setName(eventName);
        apiEvent.setDate(eventDate);
        return apiEvent;
    }

    public static RegistrationApiEntity fullApiRegistration() {
        return fullApiRegistration(TestConstants.REGISTRATION_ID, TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME);
    }

    public static RegistrationApiEntity fullApiRegistration(String registrationId, String registrationFirstName,
                                                   String registrationLastName) {
        RegistrationApiEntity registration = new RegistrationApiEntity();
        registration.setId(registrationId);
        registration.setFirstName(registrationFirstName);
        registration.setLastName(registrationLastName);
        return registration;
    }

    public static HandicapGroupApiEntity fullHandicapGroup() {
        return fullHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR);
    }

    public static HandicapGroupApiEntity fullHandicapGroup(
            String handicapGroupId,
            String handicapGroupName,
            BigDecimal handicapGroupFactor) {
        HandicapGroupApiEntity handicapGroup = new HandicapGroupApiEntity();
        handicapGroup.setId(handicapGroupId);
        handicapGroup.setName(handicapGroupName);
        handicapGroup.setHandicapFactor(handicapGroupFactor);
        return handicapGroup;
    }

    public static HandicapGroupSetApiEntity fullHandicapGroupSet(
            String handicapGroupSetId,
            String handicapGroupSetName,
            Set<HandicapGroupApiEntity> handicapGroups
    ) {
        HandicapGroupSetApiEntity handicapGroupSet = new HandicapGroupSetApiEntity();
        handicapGroupSet.setId(handicapGroupSetId);
        handicapGroupSet.setName(handicapGroupSetName);
        handicapGroupSet.setHandicapGroups(handicapGroups);
        return handicapGroupSet;
    }

    public static HandicapGroupSetApiEntity fullHandicapGroupSet() {
        return fullHandicapGroupSet(
                TestConstants.HANDICAP_GROUP_SET_ID,
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(fullHandicapGroup())
        );
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSetRequest(
            String addHandicapGroupSetRequestName,
            Set<AddHandicapGroupSetRequest.HandicapGroup> addHandicapGroupSetRequestHandicapGroups
    ) {
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = new AddHandicapGroupSetRequest();
        addHandicapGroupSetRequest.setName(addHandicapGroupSetRequestName);
        addHandicapGroupSetRequest.setHandicapGroups(addHandicapGroupSetRequestHandicapGroups);
        return addHandicapGroupSetRequest;
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSetRequest() {
        return fullAddHandicapGroupSetRequest(
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(fullAddHandicapGroupSetRequestHandicapGroup())
        );
    }

    public static AddHandicapGroupSetRequest.HandicapGroup fullAddHandicapGroupSetRequestHandicapGroup(
            String handicapGroupId
    ) {
        AddHandicapGroupSetRequest.HandicapGroup handicapGroup = new AddHandicapGroupSetRequest.HandicapGroup();
        handicapGroup.setId(handicapGroupId);
        return handicapGroup;
    }

    public static AddHandicapGroupSetRequest.HandicapGroup fullAddHandicapGroupSetRequestHandicapGroup() {
        return fullAddHandicapGroupSetRequestHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID
        );
    }

    public static CompetitionGroupApiEntity fullCompetitionGroup() {
        return fullCompetitionGroup(
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name(),
                TestConstants.COMPETITION_GROUP_GROUPING
        );
    }

    public static CompetitionGroupApiEntity fullCompetitionGroup(
            String competitionGroupId,
            String competitionGroupName,
            BigDecimal competitionGroupHandicapFactor,
            String competitionGroupResultTimeType,
            boolean competitionGroupGrouping
    ) {
        CompetitionGroupApiEntity competitionGroup = new CompetitionGroupApiEntity();
        competitionGroup.setId(competitionGroupId);
        competitionGroup.setName(competitionGroupName);
        competitionGroup.setHandicapFactor(competitionGroupHandicapFactor);
        competitionGroup.setResultTimeType(competitionGroupResultTimeType);
        competitionGroup.setGrouping(competitionGroupGrouping);
        return competitionGroup;
    }

    public static CompetitionGroupSetApiEntity fullCompetitionGroupSet(
            String competitionGroupSetId,
            String competitionGroupSetName,
            Set<CompetitionGroupApiEntity> competitionGroupApiEntities
    ) {
        CompetitionGroupSetApiEntity competitionGroupSet = new CompetitionGroupSetApiEntity();
        competitionGroupSet.setId(competitionGroupSetId);
        competitionGroupSet.setName(competitionGroupSetName);
        competitionGroupSet.setCompetitionGroups(competitionGroupApiEntities);
        return competitionGroupSet;
    }

    public static CompetitionGroupSetApiEntity fullCompetitionGroupSet() {
        return fullCompetitionGroupSet(
                TestConstants.COMPETITION_GROUP_SET_ID,
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(fullCompetitionGroup())
        );
    }
}
