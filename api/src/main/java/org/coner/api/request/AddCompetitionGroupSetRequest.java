package org.coner.api.request;

import java.util.Set;

import org.hibernate.validator.constraints.NotBlank;

public class AddCompetitionGroupSetRequest {

    @NotBlank
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

    public boolean hasCompetitionGroups() {
        return competitionGroups != null && !competitionGroups.isEmpty();
    }

    public void setCompetitionGroups(Set<CompetitionGroup> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }

    /**
     * Request competition groups be added by Competition Group ID.
     */
    public static class CompetitionGroup {
        @NotBlank
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CompetitionGroup that = (CompetitionGroup) o;

            return !(id != null ? !id.equals(that.id) : that.id != null);

        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddCompetitionGroupSetRequest that = (AddCompetitionGroupSetRequest) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(competitionGroups != null
                ? !competitionGroups.equals(that.competitionGroups)
                : that.competitionGroups != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (competitionGroups != null ? competitionGroups.hashCode() : 0);
        return result;
    }
}
