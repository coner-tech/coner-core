package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

import org.coner.core.domain.entity.CompetitionGroup;

public final class TestConstants {

    private TestConstants() {
    }

    // Person
    public static final String PERSON_ID = "person-test-id";
    public static final String PERSON_FIRST_NAME = "person-first-name";
    public static final String PERSON_MIDDLE_NAME = "person-middle-name";
    public static final String PERSON_LAST_NAME = "person-last-name";

    // Event
    public static final String EVENT_ID = "event-test-id";
    public static final String EVENT_NAME = "event-test-name";
    public static final Date EVENT_DATE = Date.from(ZonedDateTime.parse("2014-12-26T19:44:00-05:00").toInstant());
    public static final int EVENT_MAX_RUNS_PER_REGISTRATION = 4;
    public static final boolean EVENT_CURRENT = false;

    //Registration
    public static final String REGISTRATION_ID = "registration-test-id";
    public static final String REGISTRATION_NUMBER = "098";
    public static final boolean REGISTRATION_CHECKED_IN = false;

    //HandicapGroup
    public static final String HANDICAP_GROUP_ID = "handicap-group-test-id";
    public static final String HANDICAP_GROUP_NAME = "handicap-group-name";
    public static final BigDecimal HANDICAP_GROUP_FACTOR = BigDecimal.valueOf(0.987d);

    // HandicapGroupSet
    public static final String HANDICAP_GROUP_SET_ID = "handicap-group-set-test-id";
    public static final String HANDICAP_GROUP_SET_NAME = "handicap-group-set-test-name";

    // CompetitionGroup
    public static final String COMPETITION_GROUP_ID = "competition-group-test-id";
    public static final String COMPETITION_GROUP_NAME = "competition-group-name";
    public static final BigDecimal COMPETITION_GROUP_FACTOR = BigDecimal.valueOf(0.876d);
    public static final CompetitionGroup.ResultTimeType COMPETITION_GROUP_RESULT_TIME_TYPE =
            CompetitionGroup.ResultTimeType.RAW;
    public static final boolean COMPETITION_GROUP_GROUPING = true;

    // CompetitionGroupSet
    public static final String COMPETITION_GROUP_SET_ID = "competition-group-set-test-id";
    public static final String COMPETITION_GROUP_SET_NAME = "competition-group-set-test-name";

    // Run
    public static final String RUN_ID = "run-id";
    public static final int RUN_SEQUENCE = 1;
    public static final Instant RUN_TIMESTAMP = Instant.parse("2017-05-29T23:50:00.00Z");
    public static final BigDecimal RUN_RAW_TIME = BigDecimal.valueOf(123.456d);
    public static final int RUN_CONES = 0;
    public static final String RUN_PENALTY = null;
    public static final boolean RUN_RERUN = false;
    public static final boolean RUN_COMPETITIVE = true;
}
