package org.coner.core.api.response;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;

public class GetCompetitionGroupSetsResponse {
    private List<CompetitionGroupSetApiEntity> competitionGroupSets;

    public List<CompetitionGroupSetApiEntity> getCompetitionGroupSets() {
        return competitionGroupSets;
    }

    public void setCompetitionGroupSets(List<CompetitionGroupSetApiEntity> competitionGroupSets) {
        this.competitionGroupSets = competitionGroupSets;
    }
}
