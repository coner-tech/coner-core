package org.coner.core.api.entity;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CompetitionGroupSetApiEntity extends ApiEntity {

    private String id;
    private String name;
    private Set<CompetitionGroupApiEntity> competitionGroups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CompetitionGroupApiEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroupApiEntity> competitionGroups) {
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
