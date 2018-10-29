package org.coner.core.util

import com.google.common.collect.Sets
import org.coner.core.domain.entity.*
import java.math.BigDecimal
import java.time.Instant
import java.time.Year
import java.util.*

object DomainEntityTestUtils {

    @JvmOverloads
    @JvmStatic
    fun fullPerson(id: String = TestConstants.PERSON_ID, firstName: String = TestConstants.PERSON_FIRST_NAME, middleName: String = TestConstants.PERSON_MIDDLE_NAME, lastName: String = TestConstants.PERSON_LAST_NAME): Person {
        val person = Person()
        person.id = id
        person.firstName = firstName
        person.middleName = middleName
        person.lastName = lastName
        return person
    }

    @JvmOverloads
    @JvmStatic
    fun fullCar(id: String = TestConstants.CAR_ID, year: Year = TestConstants.CAR_YEAR, make: String = TestConstants.CAR_MAKE, model: String = TestConstants.CAR_MODEL, trim: String = TestConstants.CAR_TRIM, color: String = TestConstants.CAR_COLOR): Car {
        val car = Car()
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
            handicapGroupSet: HandicapGroupSet = fullHandicapGroupSet(),
            competitionGroupSet: CompetitionGroupSet = fullCompetitionGroupSet(),
            maxRunsPerRegistration: Int = TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION,
            current: Boolean = TestConstants.EVENT_CURRENT
    ): Event {
        val event = Event()
        event.id = id
        event.name = name
        event.date = date
        event.handicapGroupSet = handicapGroupSet
        event.competitionGroupSet = competitionGroupSet
        event.maxRunsPerRegistration = maxRunsPerRegistration
        event.isCurrent = current
        return event
    }

    @JvmOverloads
    @JvmStatic
    fun fullRegistration(
            id: String = TestConstants.REGISTRATION_ID,
            person: Person = fullPerson(),
            car: Car = fullCar(),
            event: Event = fullEvent(),
            handicapGroup: HandicapGroup = fullHandicapGroup(),
            competitionGroup: CompetitionGroup = fullCompetitionGroup(),
            number: String = TestConstants.REGISTRATION_NUMBER,
            checkedIn: Boolean = TestConstants.REGISTRATION_CHECKED_IN
    ): Registration {
        val registration = Registration()
        registration.id = id
        registration.person = person
        registration.car = car
        registration.event = event
        registration.handicapGroup = handicapGroup
        registration.competitionGroup = competitionGroup
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
    ): HandicapGroup {
        val handicapGroup = HandicapGroup()
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
            handicapGroups: Set<HandicapGroup> = Sets.newHashSet(fullHandicapGroup())
    ): HandicapGroupSet {
        val handicapGroupSet = HandicapGroupSet()
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
            factor: BigDecimal = TestConstants.COMPETITION_GROUP_FACTOR,
            grouping: Boolean = TestConstants.COMPETITION_GROUP_GROUPING,
            resultTimeType: CompetitionGroup.ResultTimeType = TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE
    ): CompetitionGroup {
        val domainCompetitionGroup = CompetitionGroup()
        domainCompetitionGroup.id = id
        domainCompetitionGroup.name = name
        domainCompetitionGroup.factor = factor
        domainCompetitionGroup.isGrouping = grouping
        domainCompetitionGroup.resultTimeType = resultTimeType
        return domainCompetitionGroup
    }

    @JvmOverloads
    @JvmStatic
    fun fullCompetitionGroupSet(
            id: String = TestConstants.COMPETITION_GROUP_SET_ID,
            name: String = TestConstants.COMPETITION_GROUP_SET_NAME,
            competitionGroups: Set<CompetitionGroup> = Sets.newHashSet(fullCompetitionGroup())
    ): CompetitionGroupSet {
        val competitionGroupSet = CompetitionGroupSet()
        competitionGroupSet.id = id
        competitionGroupSet.name = name
        competitionGroupSet.competitionGroups = competitionGroups
        return competitionGroupSet
    }

    @JvmOverloads
    @JvmStatic
    fun fullRun(
            id: String = TestConstants.RUN_ID,
            event: Event = fullEvent(),
            registration: Registration = fullRegistration(),
            sequence: Int = TestConstants.RUN_SEQUENCE,
            timestamp: Instant = TestConstants.RUN_TIMESTAMP,
            rawTime: BigDecimal = TestConstants.RUN_RAW_TIME,
            cones: Int = TestConstants.RUN_CONES,
            didNotFinish: Boolean = TestConstants.RUN_DID_NOT_FINISH,
            disqualified: Boolean = TestConstants.RUN_DISQUALIFIED,
            rerun: Boolean = TestConstants.RUN_RERUN,
            competitive: Boolean = TestConstants.RUN_COMPETITIVE
    ) = Run(
            id = id,
            event = event,
            registration = registration,
            sequence = sequence,
            timestamp = timestamp,
            rawTime = rawTime,
            cones = cones,
            didNotFinish = didNotFinish,
            disqualified = disqualified,
            rerun = rerun,
            competitive = competitive
    )

    @JvmOverloads
    @JvmStatic
    fun fullScoredRun(
            run: Run = fullRun(),
            rawTimeScored: BigDecimal = TestConstants.SCORED_RUN_RAW_TIME_SCORED, handicapTimeScored: BigDecimal = TestConstants.SCORED_RUN_HANDICAP_TIME_SCORED): ScoredRun {
        val scoredRun = ScoredRun()
        scoredRun.run = run
        scoredRun.rawTimeScored = rawTimeScored
        scoredRun.handicapTimeScored = handicapTimeScored
        return scoredRun
    }

}
