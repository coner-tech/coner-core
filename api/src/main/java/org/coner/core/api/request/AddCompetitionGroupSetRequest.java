package org.coner.core.api.request;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddCompetitionGroupSetRequest {

    @NotBlank
    private String name;
    private Set<String> competitionGroupIds;

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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
