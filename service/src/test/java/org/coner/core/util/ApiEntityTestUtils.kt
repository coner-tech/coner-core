package org.coner.core.util

import com.google.common.collect.Sets
import org.coner.core.api.entity.*
import java.math.BigDecimal
import java.time.Instant
import java.time.Year
import java.util.*

object ApiEntityTestUtils {

    @JvmOverloads
    @JvmStatic
    fun fullPerson(id: String = TestConstants.PERSON_ID, firstName: String = TestConstants.PERSON_FIRST_NAME, middleName: String = TestConstants.PERSON_MIDDLE_NAME, lastName: String = TestConstants.PERSON_LAST_NAME): PersonApiEntity {
        val person = PersonApiEntity()
        person.id = id
        person.firstName = firstName
        person.middleName = middleName
        person.lastName = lastName
        return person
    }

    @JvmOverloads
    @JvmStatic
    fun fullCar(id: String = TestConstants.CAR_ID, year: Year = TestConstants.CAR_YEAR, make: String = TestConstants.CAR_MAKE, model: String = TestConstants.CAR_MODEL, trim: String = TestConstants.CAR_TRIM, color: String = TestConstants.CAR_COLOR): CarApiEntity {
        val car = CarApiEntity()
        car.id = id
        car.year = year
        car.make = make
        car.model = model
        car.trim = trim
        car.color = color
        return car
    }

    @JvmOverloads
    @JvmStatic
    fun fullEvent(
            id: String = TestConstants.EVENT_ID,
            name: String = TestConstants.EVENT_NAME,
            date: Date = TestConstants.EVENT_DATE,
            handicapGroupSetId: String = TestConstants.HANDICAP_GROUP_SET_ID,
            competitionGroupSetId: String = TestConstants.COMPETITION_GROUP_SET_ID,
            maxRunsPerRegistration: Int = TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION,
            current: Boolean = TestConstants.EVENT_CURRENT
    ): EventApiEntity {
        val event = EventApiEntity()
        event.id = id
        event.name = name
        event.date = date
        event.handicapGroupSetId = handicapGroupSetId
        event.competitionGroupSetId = competitionGroupSetId
        event.maxRunsPerRegistration = maxRunsPerRegistration
        event.isCurrent = current
        return event
    }

    @JvmOverloads
    @JvmStatic
    fun fullRegistration(
            id: String = TestConstants.REGISTRATION_ID,
            person: PersonApiEntity = fullPerson(),
            car: CarApiEntity = fullCar(),
            handicapGroupId: String = TestConstants.HANDICAP_GROUP_ID,
            competitionGroupId: String = TestConstants.COMPETITION_GROUP_ID,
            number: String = TestConstants.REGISTRATION_NUMBER,
            checkedIn: Boolean = TestConstants.REGISTRATION_CHECKED_IN
    ): RegistrationApiEntity {
        val registration = RegistrationApiEntity()
        registration.id = id
        registration.person = person
        registration.car = car
        registration.handicapGroupId = handicapGroupId
        registration.competitionGroupId = competitionGroupId
        registration.number = number
        registration.isCheckedIn = checkedIn
        return registration
    }

    @JvmOverloads
    @JvmStatic
    fun fullHandicapGroup(
            id: String = TestConstants.HANDICAP_GROUP_ID,
            name: String = TestConstants.HANDICAP_GROUP_NAME,
            factor: BigDecimal = TestConstants.HANDICAP_GROUP_FACTOR
    ): HandicapGroupApiEntity {
        val handicapGroup = HandicapGroupApiEntity()
        handicapGroup.id = id
        handicapGroup.name = name
        handicapGroup.factor = factor
        return handicapGroup
    }

    @JvmOverloads
    @JvmStatic
    fun fullHandicapGroupSet(
            id: String = TestConstants.HANDICAP_GROUP_SET_ID,
            name: String = TestConstants.HANDICAP_GROUP_SET_NAME,
            handicapGroups: Set<HandicapGroupApiEntity> = Sets.newHashSet(fullHandicapGroup())
    ): HandicapGroupSetApiEntity {
        val handicapGroupSet = HandicapGroupSetApiEntity()
        handicapGroupSet.id = id
        handicapGroupSet.name = name
        handicapGroupSet.handicapGroups = handicapGroups
        return handicapGroupSet
    }

    @JvmOverloads
    @JvmStatic
    fun fullCompetitionGroup(
            id: String = TestConstants.COMPETITION_GROUP_ID,
            name: String = TestConstants.COMPETITION_GROUP_NAME,
            handicapFactor: BigDecimal = TestConstants.COMPETITION_GROUP_FACTOR,
            resultTimeType: String = TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.name,
            grouping: Boolean = TestConstants.COMPETITION_GROUP_GROUPING
    ): CompetitionGroupApiEntity {
        val competitionGroup = CompetitionGroupApiEntity()
        competitionGroup.id = id
        competitionGroup.name = name
        competitionGroup.factor = handicapFactor
        competitionGroup.resultTimeType = resultTimeType
        competitionGroup.isGrouping = grouping
        return competitionGroup
    }

    @JvmOverloads
    @JvmStatic
    fun fullCompetitionGroupSet(
            id: String = TestConstants.COMPETITION_GROUP_SET_ID,
            name: String = TestConstants.COMPETITION_GROUP_SET_NAME,
            competitionGroups: Set<CompetitionGroupApiEntity> = Sets.newHashSet(fullCompetitionGroup())
    ): CompetitionGroupSetApiEntity {
        val competitionGroupSet = CompetitionGroupSetApiEntity()
        competitionGroupSet.id = id
        competitionGroupSet.name = name
        competitionGroupSet.competitionGroups = competitionGroups
        return competitionGroupSet
    }

    @JvmOverloads
    @JvmStatic
    fun fullRun(
           id: String = TestConstants.RUN_ID,
           eventId: String = TestConstants.EVENT_ID,
           registrationId: String = TestConstants.REGISTRATION_ID,
           sequence: Int = TestConstants.RUN_SEQUENCE,
           timestamp: Instant = TestConstants.RUN_TIMESTAMP,
           rawTime: BigDecimal = TestConstants.RUN_RAW_TIME,
           cones: Int = TestConstants.RUN_CONES,
           didNotFinish: Boolean = TestConstants.RUN_DID_NOT_FINISH,
           disqualified: Boolean = TestConstants.RUN_DISQUALIFIED,
           rerun: Boolean = TestConstants.RUN_RERUN,
           competitive: Boolean = TestConstants.RUN_COMPETITIVE
    ) = RunApiEntity(
                id = id,
                eventId = eventId,
                registrationId = registrationId,
                sequence = sequence,
                timestamp = timestamp,
                rawTime = rawTime,
                cones = cones,
                didNotFinish = didNotFinish,
                disqualified = disqualified,
                rerun = rerun,
                competitive = competitive
        )
}
