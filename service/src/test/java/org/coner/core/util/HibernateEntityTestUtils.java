package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;

import com.google.common.collect.Sets;

/**
 *
 */
public final class HibernateEntityTestUtils {

    private HibernateEntityTestUtils() {
    }

    public static EventHibernateEntity fullEvent() {
        return fullEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static EventHibernateEntity fullEvent(String id, String name, Date date) {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        return event;
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

}
