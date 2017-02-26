package org.coner.core.util;

import java.math.BigDecimal;
import java.util.Set;

import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroup;

import com.google.common.collect.Sets;

public final class ApiRequestTestUtils {

    private ApiRequestTestUtils() {

    }

    public static AddCompetitionGroupRequest fullAddCompetitionGroupRequest() {
        return fullAddCompetitionGroupRequest(
                TestConstants.COMPETITION_GROUP_NAME,
                TestConstants.COMPETITION_GROUP_GROUPING,
                TestConstants.COMPETITION_GROUP_HANDICAP_FACTOR,
                TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE
        );
    }

    private static AddCompetitionGroupRequest fullAddCompetitionGroupRequest(
            String name,
            boolean grouping,
            BigDecimal handicapFactor,
            CompetitionGroup.ResultTimeType resultTimeType
    ) {
        AddCompetitionGroupRequest addCompetitionGroupRequest = new AddCompetitionGroupRequest();
        addCompetitionGroupRequest.setName(name);
        addCompetitionGroupRequest.setGrouping(grouping);
        addCompetitionGroupRequest.setHandicapFactor(handicapFactor);
        addCompetitionGroupRequest.setResultTimeType(resultTimeType.name());
        return addCompetitionGroupRequest;
    }

    public static AddCompetitionGroupSetRequest fullAddCompetitionGroupSetRequest() {
        return fullAddCompetitionGroupSetRequest(
                TestConstants.COMPETITION_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID)
        );
    }

    private static AddCompetitionGroupSetRequest fullAddCompetitionGroupSetRequest(
            String competitionGroupSetName,
            Set<String> competitionGroupSetIds
    ) {
        AddCompetitionGroupSetRequest addCompetitionGroupSetRequest = new AddCompetitionGroupSetRequest();
        addCompetitionGroupSetRequest.setName(competitionGroupSetName);
        addCompetitionGroupSetRequest.setCompetitionGroupIds(competitionGroupSetIds);
        return addCompetitionGroupSetRequest;
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSetRequest(
            String name,
            Set<String> handicapGroupIds
    ) {
        AddHandicapGroupSetRequest addHandicapGroupSetRequest = new AddHandicapGroupSetRequest();
        addHandicapGroupSetRequest.setName(name);
        addHandicapGroupSetRequest.setHandicapGroupIds(handicapGroupIds);
        return addHandicapGroupSetRequest;
    }

    public static AddHandicapGroupSetRequest fullAddHandicapGroupSetRequest() {
        return fullAddHandicapGroupSetRequest(
                TestConstants.HANDICAP_GROUP_SET_NAME,
                Sets.newHashSet(TestConstants.HANDICAP_GROUP_ID)
        );
    }
}
