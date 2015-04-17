package org.coner.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Hibernate entity for the persistence of CompetitionGroups.
 */
@Entity
@Table(name = "competition_groups")
@NamedQueries({
        @NamedQuery(
                name = CompetitionGroup.QUERY_FIND_ALL,
                query = "from CompetitionGroup"
        )
})
public class CompetitionGroup extends HibernateEntity {

    public static final String QUERY_FIND_ALL = "org.coner.hibernate.entity.CompetitionGroup.findAll";

    private String id;
    private String name;
    private BigDecimal handicapFactor;
    private boolean grouping;
    private String resultTimeType;

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

    @Column(name = "handicap_factor")
    public BigDecimal getHandicapFactor() {
        return handicapFactor;
    }

    public void setHandicapFactor(BigDecimal handicapFactor) {
        this.handicapFactor = handicapFactor;
    }

    @Column(name = "grouping")
    public boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
    }

    @Column(name = "result_time_type")
    public String getResultTimeType() {
        return resultTimeType;
    }

    public void setResultTimeType(String resultTimeType) {
        this.resultTimeType = resultTimeType;
    }
}
