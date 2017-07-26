package org.coner.core.util;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.Set;

import org.coner.core.hibernate.entity.CarHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.PersonHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.hibernate.entity.RunHibernateEntity;

import com.google.common.collect.Sets;

public final class HibernateEntityTestUtils {

    private HibernateEntityTestUtils() {
    }

    public static PersonHibernateEntity fullPerson() {
        return fullPerson(
                TestConstants.PERSON_ID,
                TestConstants.PERSON_FIRST_NAME,
                TestConstants.PERSON_MIDDLE_NAME,
                TestConstants.PERSON_LAST_NAME
        );
    }

    public static PersonHibernateEntity fullPerson(String id, String firstName, String middleName, String lastName) {
        PersonHibernateEntity person = new PersonHibernateEntity();
        person.setId(id);
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        return person;
    }

    public static CarHibernateEntity fullCar() {
        return fullCar(
                TestConstants.CAR_ID,
                TestConstants.CAR_YEAR,
                TestConstants.CAR_MAKE,
                TestConstants.CAR_MODEL,
                TestConstants.CAR_TRIM,
                TestConstants.CAR_COLOR
        );
    }

    public static CarHibernateEntity fullCar(
            String id,
            Year year,
            String make,
            String model,
            String trim,
            String color
    ) {
        CarHibernateEntity car = new CarHibernateEntity();
        car.setId(id);
        car.setYear(year);
        car.setMake(make);
        car.setModel(model);
        car.setTrim(trim);
        car.setColor(color);
        return car;
    }

    public static EventHibernateEntity fullEvent() {
        return fullEvent(
                TestConstants.EVENT_ID,
                TestConstants.EVENT_NAME,
                TestConstants.EVENT_DATE,
                fullHandicapGroupSet(),
                fullCompetitionGroupSet(),
                TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION,
                TestConstants.EVENT_CURRENT
        );
    }

    public static EventHibernateEntity fullEvent(
            String id,
            String name,
            Date date,
            HandicapGroupSetHibernateEntity handicapGroupSet,
            CompetitionGroupSetHibernateEntity competitionGroupSet,
            int maxRunsPerRegistration,
            boolean current
    ) {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        event.setHandicapGroupSet(handicapGroupSet);
        event.setCompetitionGroupSet(competitionGroupSet);
        event.setMaxRunsPerRegistration(maxRunsPerRegistration);
        event.setCurrent(current);
        return event;
    }

    public static RegistrationHibernateEntity fullRegistration() {
        return fullRegistration(
                TestConstants.REGISTRATION_ID,
                fullPerson(),
                fullCar(),
                fullEvent(),
                fullHandicapGroup(),
                fullCompetitionGroup(),
                TestConstants.REGISTRATION_NUMBER,
                TestConstants.REGISTRATION_CHECKED_IN
        );
    }

    public static RegistrationHibernateEntity fullRegistration(
            String id,
            PersonHibernateEntity person,
            CarHibernateEntity car,
            EventHibernateEntity event,
            HandicapGroupHibernateEntity handicapGroup,
            CompetitionGroupHibernateEntity competitionGroup,
            String number,
            boolean checkedIn
    ) {
        RegistrationHibernateEntity registration = new RegistrationHibernateEntity();
        registration.setId(id);
        registration.setPerson(person);
        registration.setCar(car);
        registration.setEvent(event);
        registration.setHandicapGroup(handicapGroup);
        registration.setCompetitionGroup(competitionGroup);
        registration.setNumber(number);
        registration.setCheckedIn(checkedIn);
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
        return RunHibernateEntity.builder()
                .id(TestConstants.RUN_ID)
                .event(fullEvent())
                .registration(fullRegistration())
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
