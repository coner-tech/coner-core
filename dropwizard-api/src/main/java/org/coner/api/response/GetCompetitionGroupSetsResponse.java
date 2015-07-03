package org.coner.api.response;

import org.coner.api.entity.CompetitionGroupSetApiEntity;

import java.util.List;

public class GetCompetitionGroupSetsResponse {
    private List<CompetitionGroupSetApiEntity> competitionGroupSets;

    public List<CompetitionGroupSetApiEntity> getCompetitionGroupSets() {
        return competitionGroupSets;
    }

    public void setCompetitionGroupSets(List<CompetitionGroupSetApiEntity> competitionGroupSets) {
        this.competitionGroupSets = competitionGroupSets;
    }
}
