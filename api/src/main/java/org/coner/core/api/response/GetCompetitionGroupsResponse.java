package org.coner.core.api.response;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupApiEntity;

public class GetCompetitionGroupsResponse {
    private List<CompetitionGroupApiEntity> competitionGroups;

    public List<CompetitionGroupApiEntity> getCompetitionGroups() {
        return competitionGroups;
    }

    public void setCompetitionGroups(List<CompetitionGroupApiEntity> competitionGroups) {
        this.competitionGroups = competitionGroups;
    }
}
