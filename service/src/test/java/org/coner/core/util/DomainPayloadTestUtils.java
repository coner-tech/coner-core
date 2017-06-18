package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.payload.RegistrationAddPayload;

import com.google.common.collect.Sets;

public final class DomainPayloadTestUtils {

    private DomainPayloadTestUtils() {

    }

    public static EventAddPayload fullEventAdd() {
        return fullEventAdd(
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                DomainEntityTestUtils.fullHandicapGroupSet(),
                DomainEntityTestUtils.fullCompetitionGroupSet(),
                TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION
        );
    }

    public static EventAddPayload fullEventAdd(
            String name,
            Date date,
            HandicapGroupSet handicapGroupSet,
            CompetitionGroupSet competitionGroupSet,
            int maxRunsPerRegistration
    ) {
        EventAddPayload eventAddPayload = new EventAddPayload();
        eventAddPayload.setName(name);
        eventAddPayload.setDate(date);
        eventAddPayload.setHandicapGroupSet(handicapGroupSet);
        eventAddPayload.setCompetitionGroupSet(competitionGroupSet);
        eventAddPayload.setMaxRunsPerRegistration(maxRunsPerRegistration);
        return eventAddPayload;
    }

    public static HandicapGroupAddPayload fullHandicapGroupAdd() {
        return fullHandicapGroupAdd(
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR
        );
    }

    public static HandicapGroupAddPayload fullHandicapGroupAdd(
            String name,
            BigDecimal handicapFactor
    ) {
        HandicapGroupAddPayload handicapGroupAddPayload = new HandicapGroupAddPayload();
        handicapGroupAddPayload.setName(name);
        handicapGroupAddPayload.setFactor(handicapFactor);
        return handicapGroupAddPayload;
    }

    public static HandicapGroupSetAddPayload fullHandicapGroupSetAdd() {
        return fullHandicapGroupSetAdd(
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.HANDICAP_GROUP_ID),
                Sets.newHashSet(DomainEntityTestUtils.fullHandicapGroup())
        );
    }

    public static HandicapGroupSetAddPayload fullHandicapGroupSetAdd(
            String name,
            Set<String> handicapGroupIds,
            Set<HandicapGroup> handicapGroups
    ) {
        HandicapGroupSetAddPayload handicapGroupSetAddPayload = new HandicapGroupSetAddPayload();
        handicapGroupSetAddPayload.setName(name);
        handicapGroupSetAddPayload.setHandicapGroupIds(handicapGroupIds);
        handicapGroupSetAddPayload.setHandicapGroups(handicapGroups);
        return handicapGroupSetAddPayload;
    }

    public static CompetitionGroupAddPayload fullCompetitionGroupAdd() {
        return fullCompetitionGroupAdd(
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name()
        );
    }

    public static CompetitionGroupAddPayload fullCompetitionGroupAdd(
            String name,
            boolean grouping,
            BigDecimal handicapFactor,
            String resultTimeType
    ) {
        CompetitionGroupAddPayload competitionGroupAddPayload = new CompetitionGroupAddPayload();
        competitionGroupAddPayload.setName(name);
        competitionGroupAddPayload.setGrouping(grouping);
        competitionGroupAddPayload.setFactor(handicapFactor);
        competitionGroupAddPayload.setResultTimeType(resultTimeType);
        return competitionGroupAddPayload;
    }

    public static CompetitionGroupSetAddPayload fullCompetitionGroupSetAdd() {
        return fullCompetitionGroupSetAdd(
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID),
                Sets.newHashSet(DomainEntityTestUtils.fullCompetitionGroup())
        );
    }

    public static CompetitionGroupSetAddPayload fullCompetitionGroupSetAdd(
            String name,
            Set<String> competitionGroupIds,
            Set<CompetitionGroup> competitionGroups
    ) {
        CompetitionGroupSetAddPayload competitionGroupSetAdd = new CompetitionGroupSetAddPayload();
        competitionGroupSetAdd.setName(name);
        competitionGroupSetAdd.setCompetitionGroupIds(competitionGroupIds);
        competitionGroupSetAdd.setCompetitionGroups(competitionGroups);
        return competitionGroupSetAdd;
    }

    public static RegistrationAddPayload fullRegistrationAdd() {
        return fullRegistrationAdd(
                TestConstants.EVENT_ID,
                DomainEntityTestUtils.fullEvent(),
                TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME
        );
    }

    public static RegistrationAddPayload fullRegistrationAdd(
            String eventId,
            Event event,
            String firstname,
            String lastname
    ) {
        RegistrationAddPayload registrationAddPayload = new RegistrationAddPayload();
        registrationAddPayload.setEventId(eventId);
        registrationAddPayload.setEvent(event);
        registrationAddPayload.setFirstName(firstname);
        registrationAddPayload.setLastName(lastname);
        return registrationAddPayload;
    }

}
