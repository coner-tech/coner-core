package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;

import com.google.common.collect.Sets;

public final class DomainEntityTestUtils {

    private DomainEntityTestUtils() {
    }

    public static Event fullEvent() {
        return fullEvent(
                TestConstants.EVENT_ID,
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                fullHandicapGroupSet(),
                fullCompetitionGroupSet(),
                TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION,
                TestConstants.EVENT_RUNNING
        );
    }

    public static Event fullEvent(
            String id,
            String name,
            Date date,
            HandicapGroupSet handicapGroupSet,
            CompetitionGroupSet competitionGroupSet,
            int maxRunsPerRegistration,
            boolean running
    ) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        event.setHandicapGroupSet(handicapGroupSet);
        event.setCompetitionGroupSet(competitionGroupSet);
        event.setMaxRunsPerRegistration(maxRunsPerRegistration);
        event.setRunning(running);
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
                TestConstants.COMPETITION_GROUP_FACTOR,
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
        domainCompetitionGroup.setFactor(factor);
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

    public static Run fullRun() {
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

    public static Run fullRun(
            String id,
            Event event,
            Registration registration,
            int sequence,
            Instant timestamp,
            BigDecimal rawTime,
            int cones,
            String penalty,
            boolean rerun,
            boolean competitive
    ) {
        Run run = new Run();
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
