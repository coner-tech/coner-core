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
@Table(name = "competition_group_sets")
@NamedQueries(
        @NamedQuery(
                name = CompetitionGroupSetHibernateEntity.QUERY_FIND_ALL,
                query = "from CompetitionGroupSetHibernateEntity"
        )
)
public class CompetitionGroupSetHibernateEntity extends HibernateEntity {

    public static final String QUERY_FIND_ALL =
            "org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity.findAll";
    private String id;
    private String name;
    private Set<CompetitionGroupHibernateEntity> competitionGroups;

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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "competitionGroupSets")
    public Set<CompetitionGroupHibernateEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroupHibernateEntity> competitionGroups) {
        this.competitionGroups = competitionGroups;
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
