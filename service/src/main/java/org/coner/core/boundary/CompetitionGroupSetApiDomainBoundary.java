package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupSetApiDomainBoundary extends AbstractBoundary<
        CompetitionGroupSetApiEntity,
        CompetitionGroupSet> {

    @Inject
    public CompetitionGroupSetApiDomainBoundary() {
    }

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
