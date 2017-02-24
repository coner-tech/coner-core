package org.coner.core.api.request;

import java.util.Objects;
import java.util.Set;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCompetitionGroupSetRequest that = (AddCompetitionGroupSetRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(competitionGroupIds, that.competitionGroupIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, competitionGroupIds);
    }
}
