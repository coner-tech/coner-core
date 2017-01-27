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

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "competition_group_sets")
@NamedQueries(
        @NamedQuery(
                name = CompetitionGroupSetHibernateEntity.QUERY_FIND_ALL,
                query = "from CompetitionGroupSetHibernateEntity"
        )
)
public class CompetitionGroupSetHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL = "org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity.findAll";
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
