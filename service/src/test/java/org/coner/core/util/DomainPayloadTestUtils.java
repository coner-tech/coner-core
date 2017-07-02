package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Year;
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
                fullPersonAdd(),
                fullCarAdd(),
                TestConstants.EVENT_ID,
                DomainEntityTestUtils.fullEvent(),
                DomainEntityTestUtils.fullHandicapGroup(),
                DomainEntityTestUtils.fullCompetitionGroup(),
                TestConstants.REGISTRATION_NUMBER,
                TestConstants.REGISTRATION_CHECKED_IN
        );
    }

    public static RegistrationAddPayload fullRegistrationAdd(
            RegistrationAddPayload.PersonAddPayload person,
            RegistrationAddPayload.CarAddPayload car,
            String eventId,
            Event event,
            HandicapGroup handicapGroup,
            CompetitionGroup competitionGroup,
            String number,
            boolean checkedIn
    ) {
        RegistrationAddPayload registrationAddPayload = new RegistrationAddPayload();
        registrationAddPayload.setEventId(eventId);
        registrationAddPayload.setEvent(event);
        registrationAddPayload.setPerson(person);
        registrationAddPayload.setCar(car);
        registrationAddPayload.setHandicapGroup(handicapGroup);
        registrationAddPayload.setCompetitionGroup(competitionGroup);
        registrationAddPayload.setNumber(number);
        registrationAddPayload.setCheckedIn(checkedIn);
        return registrationAddPayload;
    }

    public static RegistrationAddPayload.PersonAddPayload fullPersonAdd() {
        return fullPersonAdd(
                TestConstants.PERSON_FIRST_NAME,
                TestConstants.PERSON_MIDDLE_NAME,
                TestConstants.PERSON_LAST_NAME
        );
    }

    public static RegistrationAddPayload.PersonAddPayload fullPersonAdd(
            String firstName,
            String middleName,
            String lastName
    ) {
        RegistrationAddPayload.PersonAddPayload personAddPayload = new RegistrationAddPayload.PersonAddPayload();
        personAddPayload.setFirstName(firstName);
        personAddPayload.setMiddleName(middleName);
        personAddPayload.setLastName(lastName);
        return personAddPayload;
    }

    public static RegistrationAddPayload.CarAddPayload fullCarAdd() {
        return fullCarAdd(
                TestConstants.CAR_YEAR,
                TestConstants.CAR_MAKE,
                TestConstants.CAR_MODEL,
                TestConstants.CAR_TRIM,
                TestConstants.CAR_COLOR
        );
    }

    public static RegistrationAddPayload.CarAddPayload fullCarAdd(
            Year year,
            String make,
            String model,
            String trim,
            String color
    ) {
        RegistrationAddPayload.CarAddPayload carAddPayload = new RegistrationAddPayload.CarAddPayload();
        carAddPayload.setYear(year);
        carAddPayload.setMake(make);
        carAddPayload.setModel(model);
        carAddPayload.setTrim(trim);
        carAddPayload.setColor(color);
        return carAddPayload;
    }

}
