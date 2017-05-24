package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.entity.Registration;

import com.google.common.collect.Sets;

public final class DomainEntityTestUtils {

    private DomainEntityTestUtils() {
    }

    public static Event fullEvent() {
        return fullEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullEvent(String id, String name, Date date) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        return event;
    }

    public static Registration fullRegistration() {
        return fullRegistration(
                TestConstants.REGISTRATION_ID,
                TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME
        );
    }

    public static Registration fullRegistration(
            String id,
            String firstName,
            String lastName
    ) {
        Registration registration = new Registration();
        registration.setId(id);
        registration.setFirstName(firstName);
        registration.setLastName(lastName);
        registration.setEvent(DomainEntityTestUtils.fullEvent());
        return registration;
    }

    public static HandicapGroup fullHandicapGroup() {
        return fullHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR
        );
    }

    public static HandicapGroup fullHandicapGroup(
            String id,
            String name,
            BigDecimal factor
    ) {
        HandicapGroup handicapGroup = new HandicapGroup();
        handicapGroup.setId(id);
        handicapGroup.setName(name);
        handicapGroup.setFactor(factor);
        return handicapGroup;
    }

    public static HandicapGroupSet fullHandicapGroupSet(
            String id,
            String name,
            Set<HandicapGroup> handicapGroups
    ) {
        HandicapGroupSet handicapGroupSet = new HandicapGroupSet();
        handicapGroupSet.setId(id);
        handicapGroupSet.setName(name);
        handicapGroupSet.setHandicapGroups(handicapGroups);
        return handicapGroupSet;
    }

    public static HandicapGroupSet fullHandicapGroupSet() {
        return fullHandicapGroupSet(
                TestConstants.HANDICAP_GROUP_SET_ID,
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(fullHandicapGroup())
        );
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
            String id,
            String name,
            BigDecimal factor,
            boolean grouping,
            CompetitionGroup.ResultTimeType resultTimeType
    ) {
        CompetitionGroup domainCompetitionGroup = new CompetitionGroup();
        domainCompetitionGroup.setId(id);
        domainCompetitionGroup.setName(name);
        domainCompetitionGroup.setHandicapFactor(factor);
        domainCompetitionGroup.setGrouping(grouping);
        domainCompetitionGroup.setResultTimeType(resultTimeType);
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
            String id,
            String name,
            Set<CompetitionGroup> competitionGroups
    ) {
        CompetitionGroupSet competitionGroupSet = new CompetitionGroupSet();
        competitionGroupSet.setId(id);
        competitionGroupSet.setName(name);
        competitionGroupSet.setCompetitionGroups(competitionGroups);
        return competitionGroupSet;
    }

}
