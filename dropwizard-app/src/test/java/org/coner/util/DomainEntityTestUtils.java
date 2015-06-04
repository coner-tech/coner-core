package org.coner.util;

import org.coner.core.domain.*;

import com.google.common.collect.Sets;
import java.math.BigDecimal;
import java.util.*;

public final class DomainEntityTestUtils {

    private DomainEntityTestUtils() {
    }

    public static Event fullDomainEvent() {
        return fullDomainEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullDomainEvent(String eventId, String eventName, Date eventDate) {
        Event domainEvent = new Event();
        domainEvent.setId(eventId);
        domainEvent.setName(eventName);
        domainEvent.setDate(eventDate);
        return domainEvent;
    }

    public static Registration fullDomainRegistration() {
        return fullDomainRegistration(TestConstants.REGISTRATION_ID, TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME);
    }

    public static Registration fullDomainRegistration(String registrationId, String registrationFirstName,
                                                      String registrationLastName) {
        Registration domainRegistration = new Registration();
        domainRegistration.setId(registrationId);
        domainRegistration.setFirstName(registrationFirstName);
        domainRegistration.setLastName(registrationLastName);
        domainRegistration.setEvent(DomainEntityTestUtils.fullDomainEvent());
        return domainRegistration;
    }

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
        HandicapGroup domainHandicapGroup = new HandicapGroup();
        domainHandicapGroup.setId(handicapGroupId);
        domainHandicapGroup.setName(handicapGroupName);
        domainHandicapGroup.setHandicapFactor(handicapGroupFactor);
        return domainHandicapGroup;
    }

    public static CompetitionGroup fullCompetitionGroup() {
        return fullCompetitionGroup(
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE
        );
    }

    public static CompetitionGroup fullCompetitionGroup(
            String competitionGroupId,
            String competitionGroupName,
            BigDecimal competitionGroupHandicapFactor,
            boolean competitionGroupGrouping,
            CompetitionGroup.ResultTimeType competitionGroupResultTimeType
    ) {
        CompetitionGroup domainCompetitionGroup = new CompetitionGroup();
        domainCompetitionGroup.setId(competitionGroupId);
        domainCompetitionGroup.setName(competitionGroupName);
        domainCompetitionGroup.setHandicapFactor(competitionGroupHandicapFactor);
        domainCompetitionGroup.setGrouping(competitionGroupGrouping);
        domainCompetitionGroup.setResultTimeType(competitionGroupResultTimeType);
        return domainCompetitionGroup;
    }

    public static CompetitionGroupSet fullCompetitionGroupSet() {
        return fullCompetitionGroupSet(
                TestConstants.COMPETITION_GROUP_SET_ID,
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(fullCompetitionGroup())
        );
    }

    public static CompetitionGroupSet fullCompetitionGroupSet(
            String competitionGroupSetId,
            String competitionGroupSetName,
            Set<CompetitionGroup> competitionGroups
    ) {
        CompetitionGroupSet competitionGroupSet = new CompetitionGroupSet();
        competitionGroupSet.setId(competitionGroupSetId);
        competitionGroupSet.setName(competitionGroupSetName);
        competitionGroupSet.setCompetitionGroups(competitionGroups);
        return competitionGroupSet;
    }

}
