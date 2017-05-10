package org.coner.core.domain.payload;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.CompetitionGroup;

public class CompetitionGroupSetAddPayload extends DomainAddPayload {
    private String name;
    private Set<String> competitionGroupIds;
    private Set<CompetitionGroup> competitionGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getCompetitionGroupIds() {
        return competitionGroupIds;
    }

    public void setCompetitionGroupIds(Set<String> competitionGroupIds) {
        this.competitionGroupIds = competitionGroupIds;
    }

    public Set<CompetitionGroup> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroup> competitionGroups) {
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
