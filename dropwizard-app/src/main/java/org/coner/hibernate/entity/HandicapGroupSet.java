package org.coner.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Hibernate entity for the persistence of HandicapGroupSets
 */
@Entity
@Table(name = "handicap_group_sets")
public class HandicapGroupSet extends HibernateEntity {

    private String handicapGroupSetId;
    private String name;
    private Set<HandicapGroup> handicapGroups;

    @Id
    @Column(name = "handicapGroupSetId")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getHandicapGroupSetId() {
        return handicapGroupSetId;
    }

    public void setHandicapGroupSetId(String handicapGroupSetId) {
        this.handicapGroupSetId = handicapGroupSetId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "handicapGroupSets")
    public Set<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
