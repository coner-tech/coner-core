package org.coner.api.response;

import org.coner.api.entity.CompetitionGroupApiEntity;

import java.util.List;

public class GetCompetitionGroupsResponse {
    private List<CompetitionGroupApiEntity> competitionGroups;

    public List<CompetitionGroupApiEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(List<CompetitionGroupApiEntity> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
