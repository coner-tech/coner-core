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
 * Hibernate entity for the persistence of CompetitionGroupSets
 */
@Entity
@Table(name = "competition_group_sets")
public class CompetitionGroupSet extends HibernateEntity {

    private String competitionGroupSetId;
    private String name;
    private Set<CompetitionGroup> competitionGroups;

    @Id
    @Column(name = "competitionGroupSetId")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String getCompetitionGroupSetId() {
        return competitionGroupSetId;
    }

    public void setCompetitionGroupSetId(String competitionGroupSetId) {
        this.competitionGroupSetId = competitionGroupSetId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "competitionGroupSets")
    public Set<CompetitionGroup> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroup> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
