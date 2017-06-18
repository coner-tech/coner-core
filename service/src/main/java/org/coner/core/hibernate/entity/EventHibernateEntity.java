package org.coner.core.hibernate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "events")
@NamedQueries({
        @NamedQuery(
                name = EventHibernateEntity.QUERY_FIND_ALL,
                query = "from EventHibernateEntity e"
        )
})
public class EventHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL = "EventHibernateEntity.findAll";
    private String id;
    private String name;
    private Date date;
    private HandicapGroupSetHibernateEntity handicapGroupSet;
    private CompetitionGroupSetHibernateEntity competitionGroupSet;
    private int maxRunsPerRegistration;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne
    @NotNull
    public HandicapGroupSetHibernateEntity getHandicapGroupSet() {
        return handicapGroupSet;
    }

    public void setHandicapGroupSet(HandicapGroupSetHibernateEntity handicapGroupSet) {
        this.handicapGroupSet = handicapGroupSet;
    }

    @ManyToOne
    @NotNull
    public CompetitionGroupSetHibernateEntity getCompetitionGroupSet() {
        return competitionGroupSet;
    }

    public void setCompetitionGroupSet(CompetitionGroupSetHibernateEntity competitionGroupSet) {
        this.competitionGroupSet = competitionGroupSet;
    }

    @Column(name = "maxRunsPerRegistration")
    public int getMaxRunsPerRegistration() {
        return maxRunsPerRegistration;
    }

    public void setMaxRunsPerRegistration(int maxRunsPerRegistration) {
        this.maxRunsPerRegistration = maxRunsPerRegistration;
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
