package org.coner.boundary;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupSetApiDomainBoundary extends AbstractBoundary<
        CompetitionGroupSetApiEntity,
        CompetitionGroupSet> {

    private ObjectMerger<AddCompetitionGroupSetRequest, CompetitionGroupSet>
            addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger;

    public CompetitionGroupSet toRemoteEntity(AddCompetitionGroupSetRequest addCompetitionGroupSetRequest) {
        if (addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger == null) {
            addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger = new ReflectionJavaBeanMerger<>();
        }
        CompetitionGroupSet competitionGroupSet = new CompetitionGroupSet();
        addCompetitionGroupSetRequestCompetitionGroupSetEntityMerger.merge(
                addCompetitionGroupSetRequest,
                competitionGroupSet
        );
        return competitionGroupSet;
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetApiEntity, CompetitionGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroupSet, CompetitionGroupSetApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
