package org.coner.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.*;

@JsonPropertyOrder({"id", "name", "competitionGroups"})
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionGroupSetApiEntity that = (CompetitionGroupSetApiEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(competitionGroups, that.competitionGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, competitionGroups);
    }
}
