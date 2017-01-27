package org.coner.util;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;

import org.coner.core.domain.entity.CompetitionGroup;

public final class TestConstants {

    private TestConstants() {
    }

    // Event
    public static final String EVENT_ID = "event-test-id";
    public static final String EVENT_NAME = "event-test-name";

    public static final Date EVENT_DATE = Date.from(ZonedDateTime.parse("2014-12-26T19:44:00-05:00").toInstant());
    //Registration
    public static final String REGISTRATION_ID = "registration-test-id";
    public static final String REGISTRATION_FIRSTNAME = "registration-firstname";

    public static final String REGISTRATION_LASTNAME = "registration-lastname";
    //HandicapGroup
    public static final String HANDICAP_GROUP_ID = "handicap-group-test-id";
    public static final String HANDICAP_GROUP_NAME = "handicap-group-name";

    public static final BigDecimal HANDICAP_GROUP_FACTOR = BigDecimal.valueOf(0.75);
    // HandicapGroupSet
    public static final String HANDICAP_GROUP_SET_ID = "handicap-group-set-test-id";
    public static final String HANDICAP_GROUP_SET_NAME = "handicap-group-set-test-name";

    // CompetitionGroup
    public static final String COMPETITION_GROUP_ID = "competition-group-test-id";
    public static final String COMPETITION_GROUP_NAME = "competition-group-name";
    public static final BigDecimal COMPETITION_GROUP_HANDICAP_FACTOR = BigDecimal.ONE;
    public static final CompetitionGroup.ResultTimeType COMPETITION_GROUP_RESULT_TIME_TYPE =
            CompetitionGroup.ResultTimeType.RAW;
    public static final boolean COMPETITION_GROUP_GROUPING = true;

    // CompetitionGroupSet
    public static final String COMPETITION_GROUP_SET_ID = "competition-group-set-test-id";
    public static final String COMPETITION_GROUP_SET_NAME = "competition-group-set-test-name";
}
