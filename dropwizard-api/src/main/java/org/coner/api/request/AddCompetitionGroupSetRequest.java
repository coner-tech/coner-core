package org.coner.api.request;


import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

/**
 * API request body used when adding a new CompetitionGroupSet
 */
public class AddCompetitionGroupSetRequest {

    @NotEmpty
    private String name;
    private Set<CompetitionGroup> competitionGroups;

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

    /**
     * Request competition groups be added by Competition Group ID
     */
    public static class CompetitionGroup {
        @NotEmpty
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}
