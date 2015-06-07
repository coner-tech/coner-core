package org.coner.core.domain;

import java.util.Set;

/**
 * Domain entity for the representation of CompetitionGroupSets.
 */
public class CompetitionGroupSet extends DomainEntity {

    private String id;
    private String name;
    private Set<CompetitionGroup> competitionGroups;

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

    public Set<CompetitionGroup> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(Set<CompetitionGroup> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }

}