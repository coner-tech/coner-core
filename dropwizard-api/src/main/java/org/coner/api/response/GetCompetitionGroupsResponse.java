package org.coner.api.response;

import org.coner.api.entity.CompetitionGroup;

import java.util.List;

/**
 * API response object containing a list of CompeititionGroup entities.
 */

public class GetCompetitionGroupsResponse {
    private List<CompetitionGroup> competitionGroups;

    public List<CompetitionGroup> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(List<CompetitionGroup> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
