package org.coner.util;

import org.coner.api.entity.CompetitionGroup;
import org.coner.api.entity.Event;
import org.coner.api.entity.HandicapGroup;
import org.coner.api.entity.Registration;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 */
public final class ApiEntityTestUtils {

    private ApiEntityTestUtils() {
    }

    // Event
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

    // Registration
    public static Registration fullApiRegistration() {
        return fullApiRegistration(TestConstants.REGISTRATION_ID, TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME);
    }

    public static Registration fullApiRegistration(String registrationId, String registrationFirstName,
                                                   String registrationLastName) {
        Registration apiRegistration = new Registration();
        apiRegistration.setId(registrationId);
        apiRegistration.setFirstName(registrationFirstName);
        apiRegistration.setLastName(registrationLastName);
        return apiRegistration;
    }

    // HandicapGroup
    public static HandicapGroup fullHandicapGroup() {
        return fullHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR);
    }

    public static HandicapGroup fullHandicapGroup(
            String handicapGroupId,
            String handicapGroupName,
            BigDecimal handicapGroupFactor) {
        HandicapGroup apiHandicapGroup = new HandicapGroup();
        apiHandicapGroup.setId(handicapGroupId);
        apiHandicapGroup.setName(handicapGroupName);
        apiHandicapGroup.setHandicapFactor(handicapGroupFactor);
        return apiHandicapGroup;
    }

    // CompetitionGroup
    public static CompetitionGroup fullCompetitionGroup() {
        return fullCompetitionGroup(
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE,
                TestConstants.COMPETITION_GROUP_GROUPING
        );
    }

    public static CompetitionGroup fullCompetitionGroup(
            String competitionGroupId,
            String competitionGroupName,
            BigDecimal competitionGroupHandicapFactor,
            String competitionGroupResultTimeType,
            boolean competitionGroupGrouping
    ) {
        CompetitionGroup apiCompetitionGroup = new CompetitionGroup();
        apiCompetitionGroup.setId(competitionGroupId);
        apiCompetitionGroup.setName(competitionGroupName);
        apiCompetitionGroup.setHandicapFactor(competitionGroupHandicapFactor);
        apiCompetitionGroup.setResultTimeType(competitionGroupResultTimeType);
        apiCompetitionGroup.setGrouping(competitionGroupGrouping);
        return apiCompetitionGroup;
    }
}
