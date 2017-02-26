package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Set;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;

import com.google.common.collect.Sets;

public final class DomainPayloadTestUtils {

    private DomainPayloadTestUtils() {

    }

    public static CompetitionGroupAddPayload fullCompetitionGroupAdd() {
        return fullCompetitionGroupAdd(
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
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
        competitionGroupAddPayload.setHandicapFactor(handicapFactor);
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

}
