package org.coner.api.response;

import java.util.List;

import org.coner.api.entity.CompetitionGroupApiEntity;

public class GetCompetitionGroupsResponse {
    private List<CompetitionGroupApiEntity> competitionGroups;

    public List<CompetitionGroupApiEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(List<CompetitionGroupApiEntity> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
