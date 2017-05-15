package org.coner.core.hibernate.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "handicap_group_sets")
@NamedQueries(
    @NamedQuery(
            name = HandicapGroupSetHibernateEntity.QUERY_FIND_ALL,
            query = "from HandicapGroupSetHibernateEntity"
    )
)
public class HandicapGroupSetHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL =
            "org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity.findAll";

    private String id;
    private String name;
    private Set<HandicapGroupHibernateEntity> handicapGroups;

    @Id
    @Column(name = "id")
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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "handicapGroupSets")
    public Set<HandicapGroupHibernateEntity> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroupHibernateEntity> handicapGroups) {
        this.handicapGroups = handicapGroups;
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
