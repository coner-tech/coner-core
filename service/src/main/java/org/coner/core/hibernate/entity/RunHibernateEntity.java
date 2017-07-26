package org.coner.core.hibernate.entity;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "runs",
        uniqueConstraints = {
                @UniqueConstraint(name = "events_sequences", columnNames = {"event_id", "sequence"})
        }
)
@NamedQueries({
        @NamedQuery(
                name = RunHibernateEntity.QUERY_FIND_ALL_WITH_EVENT,
                query = "FROM RunHibernateEntity r "
                        + "WHERE r.event.id = :" + RunHibernateEntity.PARAMETER_EVENT_ID + " "
                        + "ORDER BY r.sequence ASC"
        ),
        @NamedQuery(
                name = RunHibernateEntity.QUERY_FIND_FIRST_WITHOUT_TIME_AT_EVENT,
                query = "FROM RunHibernateEntity r "
                        + "WHERE r.event.id = :" + RunHibernateEntity.PARAMETER_EVENT_ID + " "
                        + "AND r.rawTime IS NULL "
                        + "ORDER BY r.sequence ASC "
        ),
        @NamedQuery(
                name = RunHibernateEntity.QUERY_FIND_ALL_WITH_REGISTRATION,
                query = "FROM RunHibernateEntity r "
                        + "WHERE r.registration.id = :" + RunHibernateEntity.PARAMETER_REGISTRATION_ID + " "
                        + "ORDER BY r.sequence ASC"
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL_WITH_EVENT = "Run.findAllWithEvent";
    public static final String QUERY_FIND_FIRST_WITHOUT_TIME_AT_EVENT = "Run.findFirstWithoutTimeAtEvent";
    public static final String QUERY_FIND_ALL_WITH_REGISTRATION = "Run.findAllWithRegistration";
    public static final String PARAMETER_EVENT_ID = "eventId";
    public static final String PARAMETER_REGISTRATION_ID = "registrationId";

    private String id;
    private EventHibernateEntity event;
    private RegistrationHibernateEntity registration;
    private int sequence;
    private Instant timestamp;
    private BigDecimal rawTime;
    private int cones;
    private boolean didNotFinish;
    private boolean disqualified;
    private boolean rerun;
    private boolean competitive;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public EventHibernateEntity getEvent() {
        return event;
    }

    public void setEvent(EventHibernateEntity event) {
        this.event = event;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public RegistrationHibernateEntity getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationHibernateEntity registration) {
        this.registration = registration;
    }

    @Column(nullable = false)
    @Range(min = 1)
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Column
    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Column(precision = 6, scale = 3)
    public BigDecimal getRawTime() {
        return rawTime;
    }

    public void setRawTime(BigDecimal rawTime) {
        this.rawTime = rawTime;
    }

    @Column
    public int getCones() {
        return cones;
    }

    public void setCones(int cones) {
        this.cones = cones;
    }

    @Column
    public boolean isDidNotFinish() {
        return didNotFinish;
    }

    public void setDidNotFinish(boolean didNotFinish) {
        this.didNotFinish = didNotFinish;
    }

    @Column
    public boolean isDisqualified() {
        return disqualified;
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    @Column
    public boolean isRerun() {
        return rerun;
    }

    public void setRerun(boolean rerun) {
        this.rerun = rerun;
    }

    @Column(name = "competitive")
    public boolean isCompetitive() {
        return competitive;
    }

    public void setCompetitive(boolean competitive) {
        this.competitive = competitive;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
