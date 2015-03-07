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
 * Hibernate entity for the persistence of HandicapGroups.
 */
@Entity
@Table(name = "handicap_groups")
@NamedQueries({
        @NamedQuery(
                name = HandicapGroup.QUERY_FIND_ALL,
                query = "from HandicapGroup"
        )
})
public class HandicapGroup extends HibernateEntity {
    public static final String QUERY_FIND_ALL = "org.coner.hibernate.entity.HandicapGroup.findAll";

    private String id;
    private String name;
    private BigDecimal handicapFactor;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HandicapGroup that = (HandicapGroup) o;

        if (handicapFactor != null ? !handicapFactor.equals(that.handicapFactor) : that.handicapFactor != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (handicapFactor != null ? handicapFactor.hashCode() : 0);
        return result;
    }
}
