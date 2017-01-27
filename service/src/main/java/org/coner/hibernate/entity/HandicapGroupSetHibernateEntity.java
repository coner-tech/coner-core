package org.coner.hibernate.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "handicap_group_sets")
public class HandicapGroupSetHibernateEntity extends HibernateEntity {

    private String handicapGroupSetId;
    private String name;
    private Set<HandicapGroupHibernateEntity> handicapGroups;

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
    public Set<HandicapGroupHibernateEntity> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroupHibernateEntity> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
