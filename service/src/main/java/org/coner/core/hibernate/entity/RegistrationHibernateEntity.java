package org.coner.core.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "registrations")
@NamedQueries({
        @NamedQuery(
                name = RegistrationHibernateEntity.QUERY_FIND_ALL_WITH_EVENT,
                query = "FROM RegistrationHibernateEntity r "
                        + "WHERE r.event.id = :" + RegistrationHibernateEntity.PARAMETER_EVENT_ID
        )
})
public class RegistrationHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL_WITH_EVENT = "org.coner.core.hibernate.entity.RegistrationHibernateEntity"
            + ".findAllWithEvent";
    public static final String PARAMETER_EVENT_ID = "eventId";

    private String id;
    private String firstName;
    private String lastName;
    private EventHibernateEntity event;
    private HandicapGroupHibernateEntity handicapGroup;
    private CompetitionGroupHibernateEntity competitionGroup;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToOne
    public EventHibernateEntity getEvent() {
        return event;
    }

    public void setEvent(EventHibernateEntity event) {
        this.event = event;
    }

    @ManyToOne(optional = false)
    public HandicapGroupHibernateEntity getHandicapGroup() {
        return handicapGroup;
    }

    public void setHandicapGroup(HandicapGroupHibernateEntity handicapGroup) {
        this.handicapGroup = handicapGroup;
    }

    @ManyToOne(optional = false)
    public CompetitionGroupHibernateEntity getCompetitionGroup() {
        return competitionGroup;
    }

    public void setCompetitionGroup(CompetitionGroupHibernateEntity competitionGroup) {
        this.competitionGroup = competitionGroup;
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
