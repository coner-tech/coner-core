package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.entity.RegistrationApiEntity;

import com.google.common.collect.Sets;

public final class ApiEntityTestUtils {

    private ApiEntityTestUtils() {
    }

    public static EventApiEntity fullEvent() {
        return fullEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static EventApiEntity fullEvent(
            String id,
            String name,
            Date date
    ) {
        EventApiEntity event = new EventApiEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        return event;
    }

    public static RegistrationApiEntity fullRegistration() {
        return fullRegistration(
                TestConstants.REGISTRATION_ID,
                TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME
        );
    }

    public static RegistrationApiEntity fullRegistration(
            String id,
            String firstName,
            String lastName
    ) {
        RegistrationApiEntity registration = new RegistrationApiEntity();
        registration.setId(id);
        registration.setFirstName(firstName);
        registration.setLastName(lastName);
        return registration;
    }

    public static HandicapGroupApiEntity fullHandicapGroup() {
        return fullHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR
        );
    }

    public static HandicapGroupApiEntity fullHandicapGroup(
            String id,
            String name,
            BigDecimal factor
    ) {
        HandicapGroupApiEntity handicapGroup = new HandicapGroupApiEntity();
        handicapGroup.setId(id);
        handicapGroup.setName(name);
        handicapGroup.setFactor(factor);
        return handicapGroup;
    }

    public static HandicapGroupSetApiEntity fullHandicapGroupSet(
            String id,
            String name,
            Set<HandicapGroupApiEntity> handicapGroups
    ) {
        HandicapGroupSetApiEntity handicapGroupSet = new HandicapGroupSetApiEntity();
        handicapGroupSet.setId(id);
        handicapGroupSet.setName(name);
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

    public static CompetitionGroupApiEntity fullCompetitionGroup() {
        return fullCompetitionGroup(
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name(),
                TestConstants.COMPETITION_GROUP_GROUPING
        );
    }

    public static CompetitionGroupApiEntity fullCompetitionGroup(
            String id,
            String name,
            BigDecimal handicapFactor,
            String resultTimeType,
            boolean grouping
    ) {
        CompetitionGroupApiEntity competitionGroup = new CompetitionGroupApiEntity();
        competitionGroup.setId(id);
        competitionGroup.setName(name);
        competitionGroup.setFactor(handicapFactor);
        competitionGroup.setResultTimeType(resultTimeType);
        competitionGroup.setGrouping(grouping);
        return competitionGroup;
    }

    public static CompetitionGroupSetApiEntity fullCompetitionGroupSet(
            String id,
            String name,
            Set<CompetitionGroupApiEntity> competitionGroups
    ) {
        CompetitionGroupSetApiEntity competitionGroupSet = new CompetitionGroupSetApiEntity();
        competitionGroupSet.setId(id);
        competitionGroupSet.setName(name);
        competitionGroupSet.setCompetitionGroups(competitionGroups);
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
