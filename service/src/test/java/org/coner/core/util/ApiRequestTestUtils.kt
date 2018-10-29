package org.coner.core.util

import com.google.common.collect.Sets
import org.coner.core.api.request.*
import java.math.BigDecimal
import java.time.Instant
import java.time.Year
import java.util.*

object ApiRequestTestUtils {

    @JvmOverloads
    @JvmStatic
    fun fullAddEvent(
            name: String = TestConstants.EVENT_NAME,
            date: Date = TestConstants.EVENT_DATE,
            handicapGroupSetId: String = TestConstants.HANDICAP_GROUP_SET_ID,
            competitionGroupSetId: String = TestConstants.COMPETITION_GROUP_SET_ID,
            maxRunsPerRegistration: Int = TestConstants.EVENT_MAX_RUNS_PER_REGISTRATION
    ): AddEventRequest {
        val request = AddEventRequest()
        request.name = name
        request.date = date
        request.handicapGroupSetId = handicapGroupSetId
        request.competitionGroupSetId = competitionGroupSetId
        request.maxRunsPerRegistration = maxRunsPerRegistration
        return request
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddCompetitionGroup(
            name: String = TestConstants.COMPETITION_GROUP_NAME,
            grouping: Boolean = TestConstants.COMPETITION_GROUP_GROUPING,
            handicapFactor: BigDecimal = TestConstants.COMPETITION_GROUP_FACTOR,
            resultTimeType: String = TestConstants.COMPETITION_GROUP_RESULT_TIME_TYPE.toString()
    ): AddCompetitionGroupRequest {
        val addCompetitionGroupRequest = AddCompetitionGroupRequest()
        addCompetitionGroupRequest.name = name
        addCompetitionGroupRequest.grouping = grouping
        addCompetitionGroupRequest.factor = handicapFactor
        addCompetitionGroupRequest.resultTimeType = resultTimeType
        return addCompetitionGroupRequest
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddCompetitionGroupSet(
            competitionGroupSetName: String = TestConstants.COMPETITION_GROUP_SET_NAME,
            competitionGroupSetIds: Set<String> = Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID)
    ): AddCompetitionGroupSetRequest {
        val addCompetitionGroupSetRequest = AddCompetitionGroupSetRequest()
        addCompetitionGroupSetRequest.name = competitionGroupSetName
        addCompetitionGroupSetRequest.competitionGroupIds = competitionGroupSetIds
        return addCompetitionGroupSetRequest
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddHandicapGroup(
            name: String = TestConstants.HANDICAP_GROUP_NAME,
            factor: BigDecimal = TestConstants.HANDICAP_GROUP_FACTOR
    ): AddHandicapGroupRequest {
        val addHandicapGroupRequest = AddHandicapGroupRequest()
        addHandicapGroupRequest.name = name
        addHandicapGroupRequest.factor = factor
        return addHandicapGroupRequest
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddHandicapGroupSet(
            name: String = TestConstants.HANDICAP_GROUP_SET_NAME,
            handicapGroupIds: Set<String> = Sets.newHashSet(TestConstants.HANDICAP_GROUP_ID)
    ): AddHandicapGroupSetRequest {
        val addHandicapGroupSetRequest = AddHandicapGroupSetRequest()
        addHandicapGroupSetRequest.name = name
        addHandicapGroupSetRequest.handicapGroupIds = handicapGroupIds
        return addHandicapGroupSetRequest
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddRegistration(
            person: AddRegistrationRequest.AddPerson = fullAddPerson(), car: AddRegistrationRequest.AddCar = fullAddCar(), handicapGroupId: String = TestConstants.HANDICAP_GROUP_ID,
            competitionGroupId: String = TestConstants.COMPETITION_GROUP_ID,
            number: String = TestConstants.REGISTRATION_NUMBER,
            checkedIn: Boolean = TestConstants.REGISTRATION_CHECKED_IN
    ): AddRegistrationRequest {
        val addRegistrationRequest = AddRegistrationRequest()
        addRegistrationRequest.person = person
        addRegistrationRequest.car = car
        addRegistrationRequest.handicapGroupId = handicapGroupId
        addRegistrationRequest.competitionGroupId = competitionGroupId
        addRegistrationRequest.number = number
        addRegistrationRequest.isCheckedIn = checkedIn
        return addRegistrationRequest
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddPerson(
            firstName: String = TestConstants.PERSON_FIRST_NAME,
            middleName: String = TestConstants.PERSON_MIDDLE_NAME,
            lastName: String = TestConstants.PERSON_LAST_NAME
    ): AddRegistrationRequest.AddPerson {
        val addPerson = AddRegistrationRequest.AddPerson()
        addPerson.firstName = firstName
        addPerson.middleName = middleName
        addPerson.lastName = lastName
        return addPerson
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddCar(
            year: Year = TestConstants.CAR_YEAR,
            make: String = TestConstants.CAR_MAKE,
            model: String = TestConstants.CAR_MODEL,
            trim: String = TestConstants.CAR_TRIM,
            color: String = TestConstants.CAR_COLOR
    ): AddRegistrationRequest.AddCar {
        val addCar = AddRegistrationRequest.AddCar()
        addCar.year = year
        addCar.make = make
        addCar.model = model
        addCar.trim = trim
        addCar.color = color
        return addCar
    }

    @JvmOverloads
    @JvmStatic
    fun fullAddRun(
            registrationId: String = TestConstants.REGISTRATION_ID,
            timestamp: Instant = TestConstants.RUN_TIMESTAMP,
            rawTime: BigDecimal = TestConstants.RUN_RAW_TIME,
            cones: Int = TestConstants.RUN_CONES,
            didNotFinish: Boolean = TestConstants.RUN_DID_NOT_FINISH,
            disqualified: Boolean = TestConstants.RUN_DISQUALIFIED,
            rerun: Boolean = TestConstants.RUN_RERUN,
            competitive: Boolean = TestConstants.RUN_COMPETITIVE
    ) = AddRunRequest(
            registrationId = registrationId,
            timestamp = timestamp,
            rawTime = rawTime,
            cones = cones,
            isDidNotFinish = didNotFinish,
            isDisqualified = disqualified,
            isRerun = rerun,
            isCompetitive = competitive
    )
}
