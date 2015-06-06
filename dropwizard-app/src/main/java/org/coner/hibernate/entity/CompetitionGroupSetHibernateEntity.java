package org.coner.hibernate.entity;

import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "competition_group_sets")
public class CompetitionGroupSetHibernateEntity extends HibernateEntity {

    private String competitionGroupSetId;
    private String name;
    private Set<CompetitionGroupHibernateEntity> competitionGroups;

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
    public Set<CompetitionGroupHibernateEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroupHibernateEntity> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
