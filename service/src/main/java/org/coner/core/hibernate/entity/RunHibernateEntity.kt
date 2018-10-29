package org.coner.core.hibernate.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.validator.constraints.Range
import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "runs", uniqueConstraints = arrayOf(UniqueConstraint(name = "events_sequences", columnNames = arrayOf("event_id", "sequence"))))
@NamedQueries(NamedQuery(name = RunHibernateEntity.QUERY_FIND_ALL_WITH_EVENT, query = "FROM RunHibernateEntity r "
        + "WHERE r.event.id = :" + RunHibernateEntity.PARAMETER_EVENT_ID + " "
        + "ORDER BY r.sequence ASC"), NamedQuery(name = RunHibernateEntity.QUERY_FIND_FIRST_WITHOUT_TIME_AT_EVENT, query = "FROM RunHibernateEntity r "
        + "WHERE r.event.id = :" + RunHibernateEntity.PARAMETER_EVENT_ID + " "
        + "AND r.rawTime IS NULL "
        + "ORDER BY r.sequence ASC "), NamedQuery(name = RunHibernateEntity.QUERY_FIND_ALL_WITH_REGISTRATION, query = "FROM RunHibernateEntity r "
        + "WHERE r.registration.id = :" + RunHibernateEntity.PARAMETER_REGISTRATION_ID + " "
        + "ORDER BY r.sequence ASC"))
data class RunHibernateEntity(
        @get:Id
        @get:GeneratedValue(generator = "uuid")
        @get:GenericGenerator(name = "uuid", strategy = "uuid2")
        var id: String? = null,
        @get:ManyToOne(fetch = FetchType.LAZY, optional = false)
        var event: EventHibernateEntity? = null,
        @get:ManyToOne(fetch = FetchType.LAZY)
        var registration: RegistrationHibernateEntity? = null,
        @get:Column(nullable = false)
        @get:Range(min = 1)
        var sequence: Int = 0,
        @get:Column
        var timestamp: Instant? = null,
        @get:Column(precision = 6, scale = 3)
        var rawTime: BigDecimal? = null,
        @get:Column
        var cones: Int = 0,
        @get:Column
        var didNotFinish: Boolean = false,
        @get:Column
        var disqualified: Boolean = false,
        @get:Column
        var rerun: Boolean = false,
        @get:Column(name = "competitive")
        var competitive: Boolean = false
) : HibernateEntity() {

    companion object {
        const val QUERY_FIND_ALL_WITH_EVENT = "Run.findAllWithEvent"

        const val QUERY_FIND_FIRST_WITHOUT_TIME_AT_EVENT = "Run.findFirstWithoutTimeAtEvent"
        const val QUERY_FIND_ALL_WITH_REGISTRATION = "Run.findAllWithRegistration"
        const val PARAMETER_EVENT_ID = "eventId"
        const val PARAMETER_REGISTRATION_ID = "registrationId"
    }
}
