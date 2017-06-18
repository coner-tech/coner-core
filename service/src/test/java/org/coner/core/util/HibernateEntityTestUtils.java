package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.hibernate.entity.RunHibernateEntity;

import com.google.common.collect.Sets;

public final class HibernateEntityTestUtils {

    private HibernateEntityTestUtils() {
    }

    public static EventHibernateEntity fullEvent() {
        return fullEvent(
                TestConstants.EVENT_ID,
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                fullHandicapGroupSet()
        );
    }

    public static EventHibernateEntity fullEvent(
            String id,
            String name,
            Date date,
            HandicapGroupSetHibernateEntity handicapGroupSet
    ) {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        event.setHandicapGroupSet(handicapGroupSet);
        return event;
    }

    public static RegistrationHibernateEntity fullRegistration() {
        return fullRegistration(
                TestConstants.REGISTRATION_ID,
                TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME,
                fullEvent()
        );
    }

    public static RegistrationHibernateEntity fullRegistration(
            String id,
            String firstName,
            String lastName,
            EventHibernateEntity event
    ) {
        RegistrationHibernateEntity registration = new RegistrationHibernateEntity();
        registration.setId(id);
        registration.setFirstName(firstName);
        registration.setLastName(lastName);
        registration.setEvent(event);
        return registration;
    }

    public static HandicapGroupHibernateEntity fullHandicapGroup() {
        return fullHandicapGroup(
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.HANDICAP_GROUP_NAME,
                TestConstants.HANDICAP_GROUP_FACTOR
        );
    }

    public static HandicapGroupHibernateEntity fullHandicapGroup(
            String id,
            String name,
            BigDecimal factor
    ) {
        HandicapGroupHibernateEntity handicapGroup = new HandicapGroupHibernateEntity();
        handicapGroup.setId(id);
        handicapGroup.setName(name);
        handicapGroup.setFactor(factor);
        return handicapGroup;
    }

    public static HandicapGroupSetHibernateEntity fullHandicapGroupSet() {
        return fullHandicapGroupSet(
                TestConstants.HANDICAP_GROUP_SET_ID,
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(fullHandicapGroup())
        );
    }

    public static HandicapGroupSetHibernateEntity fullHandicapGroupSet(
            String id,
            String name,
            Set<HandicapGroupHibernateEntity> handicapGroups
    )  {
        HandicapGroupSetHibernateEntity handicapGroupSet = new HandicapGroupSetHibernateEntity();
        handicapGroupSet.setId(id);
        handicapGroupSet.setName(name);
        handicapGroupSet.setHandicapGroups(handicapGroups);
        return handicapGroupSet;
    }

    public static CompetitionGroupHibernateEntity fullCompetitionGroup() {
        return fullCompetitionGroup(
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name()
        );

    }

    public static CompetitionGroupHibernateEntity fullCompetitionGroup(
            String id,
            String name,
            boolean grouping,
            BigDecimal handicapFactor,
            String resultTimeType
    ) {
        CompetitionGroupHibernateEntity competitionGroup = new CompetitionGroupHibernateEntity();
        competitionGroup.setId(id);
        competitionGroup.setName(name);
        competitionGroup.setGrouping(grouping);
        competitionGroup.setFactor(handicapFactor);
        competitionGroup.setResultTimeType(resultTimeType);
        return competitionGroup;
    }

    public static CompetitionGroupSetHibernateEntity fullCompetitionGroupSet() {
        return fullCompetitionGroupSet(
                TestConstants.COMPETITION_GROUP_SET_ID,
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(fullCompetitionGroup())
        );
    }

    public static CompetitionGroupSetHibernateEntity fullCompetitionGroupSet(
            String id,
            String name,
            Set<CompetitionGroupHibernateEntity> competitionGroups
    ) {
        CompetitionGroupSetHibernateEntity competitionGroupSet = new CompetitionGroupSetHibernateEntity();
        competitionGroupSet.setId(id);
        competitionGroupSet.setName(name);
        competitionGroupSet.setCompetitionGroups(competitionGroups);
        return competitionGroupSet;
    }

    public static RunHibernateEntity fullRun() {
        return fullRun(
                TestConstants.RUN_ID,
                fullEvent(),
                fullRegistration(),
                TestConstants.RUN_SEQUENCE,
                TestConstants.RUN_TIMESTAMP,
                TestConstants.RUN_RAW_TIME,
                TestConstants.RUN_CONES,
                TestConstants.RUN_PENALTY,
                TestConstants.RUN_RERUN,
                TestConstants.RUN_COMPETITIVE
        );
    }

    public static RunHibernateEntity fullRun(
            String id,
            EventHibernateEntity event,
            RegistrationHibernateEntity registration,
            int sequence,
            Instant timestamp,
            BigDecimal rawTime,
            int cones,
            String penalty,
            boolean rerun,
            boolean competitive
    ) {
        RunHibernateEntity run = new RunHibernateEntity();
        run.setId(id);
        run.setEvent(event);
        run.setRegistration(registration);
        run.setSequence(sequence);
        run.setTimestamp(timestamp);
        run.setRawTime(rawTime);
        run.setCones(cones);
        run.setPenalty(penalty);
        run.setRerun(rerun);
        run.setCompetitive(competitive);
        return run;
    }

}
