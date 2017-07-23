package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.Set;

import org.coner.core.api.entity.CarApiEntity;
import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.entity.PersonApiEntity;
import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;

import com.google.common.collect.Sets;

public final class ApiEntityTestUtils {

    private ApiEntityTestUtils() {
    }

    public static PersonApiEntity fullPerson() {
        return fullPerson(
                TestConstants.PERSON_ID,
                TestConstants.PERSON_FIRST_NAME,
                TestConstants.PERSON_MIDDLE_NAME,
                TestConstants.PERSON_LAST_NAME
        );
    }

    public static PersonApiEntity fullPerson(String id, String firstName, String middleName, String lastName) {
        PersonApiEntity person = new PersonApiEntity();
        person.setId(id);
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        return person;
    }

    public static CarApiEntity fullCar() {
        return fullCar(
                TestConstants.CAR_ID,
                TestConstants.CAR_YEAR,
                TestConstants.CAR_MAKE,
                TestConstants.CAR_MODEL,
                TestConstants.CAR_TRIM,
                TestConstants.CAR_COLOR
        );
    }

    public static CarApiEntity fullCar(String id, Year year, String make, String model, String trim, String color) {
        CarApiEntity car = new CarApiEntity();
        car.setId(id);
        car.setYear(year);
        car.setMake(make);
        car.setModel(model);
        car.setTrim(trim);
        car.setColor(color);
        return car;
    }

    public static EventApiEntity fullEvent() {
        return fullEvent(
                TestConstants.EVENT_ID,
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                TestConstants.HANDICAP_GROUP_SET_ID,
                TestConstants.COMPETITION_GROUP_SET_ID,
                TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION,
                TestConstants.EVENT_CURRENT
        );
    }

    public static EventApiEntity fullEvent(
            String id,
            String name,
            Date date,
            String handicapGroupSetId,
            String competitionGroupSetId,
            int maxRunsPerRegistration,
            boolean current
    ) {
        EventApiEntity event = new EventApiEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        event.setHandicapGroupSetId(handicapGroupSetId);
        event.setCompetitionGroupSetId(competitionGroupSetId);
        event.setMaxRunsPerRegistration(maxRunsPerRegistration);
        event.setCurrent(current);
        return event;
    }

    public static RegistrationApiEntity fullRegistration() {
        return fullRegistration(
                TestConstants.REGISTRATION_ID,
                fullPerson(),
                fullCar(),
                TestConstants.HANDICAP_GROUP_ID,
                TestConstants.COMPETITION_GROUP_ID,
                TestConstants.REGISTRATION_NUMBER,
                TestConstants.REGISTRATION_CHECKED_IN
        );
    }

    public static RegistrationApiEntity fullRegistration(
            String id,
            PersonApiEntity person,
            CarApiEntity car,
            String handicapGroupId,
            String competitionGroupId,
            String number,
            boolean checkedIn
    ) {
        RegistrationApiEntity registration = new RegistrationApiEntity();
        registration.setId(id);
        registration.setPerson(person);
        registration.setCar(car);
        registration.setHandicapGroupId(handicapGroupId);
        registration.setCompetitionGroupId(competitionGroupId);
        registration.setNumber(number);
        registration.setCheckedIn(checkedIn);
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

    public static CompetitionGroupApiEntity fullCompetitionGroup(String id, AddCompetitionGroupRequest addRequest) {
        return fullCompetitionGroup(
                id,
                addRequest.getName(),
                addRequest.getFactor(),
                addRequest.getResultTimeType(),
                addRequest.getGrouping()
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

    public static RunApiEntity fullRun() {
        return RunApiEntity.builder()
                .id(TestConstants.RUN_ID)
                .eventId(TestConstants.EVENT_ID)
                .registrationId(TestConstants.REGISTRATION_ID)
                .sequence(TestConstants.RUN_SEQUENCE)
                .timestamp(TestConstants.RUN_TIMESTAMP)
                .rawTime(TestConstants.RUN_RAW_TIME)
                .cones(TestConstants.RUN_CONES)
                .didNotFinish(TestConstants.RUN_DID_NOT_FINISH)
                .disqualified(TestConstants.RUN_DISQUALIFIED)
                .rerun(TestConstants.RUN_RERUN)
                .competitive(TestConstants.RUN_COMPETITIVE)
                .build();
    }
}
