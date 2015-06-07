package org.coner.boundary;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.CompetitionGroupSet;

public class CompetitionGroupSetApiDomainBoundary extends AbstractBoundary<
        CompetitionGroupSetApiEntity,
        CompetitionGroupSet> {

    private EntityMerger<AddCompetitionGroupSetRequest, CompetitionGroupSet>
            addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger;

    public CompetitionGroupSet toRemoteEntity(AddCompetitionGroupSetRequest addCompetitionGroupSetRequest) {
        if (addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger == null) {
            addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger = new ReflectionEntityMerger<>();
        }
        CompetitionGroupSet competitionGroupSet = new CompetitionGroupSet();
        addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger.merge(
                addCompetitionGroupSetRequest,
                competitionGroupSet
        );
        return competitionGroupSet;
    }

    @Override
    protected EntityMerger<CompetitionGroupSetApiEntity, CompetitionGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, CompetitionGroupSetApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
