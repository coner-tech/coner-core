package org.coner.boundary;

import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.util.merger.CompositeMerger;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

import com.google.common.collect.ImmutableSet;

public class CompetitionGroupSetApiAddPayloadBoundary extends AbstractBoundary<
        AddCompetitionGroupSetRequest,
        CompetitionGroupSetAddPayload> {

    @Override
    protected ObjectMerger<AddCompetitionGroupSetRequest, CompetitionGroupSetAddPayload> buildLocalToRemoteMerger() {
        return new CompositeMerger<>(
                ReflectionPayloadJavaBeanMerger.javaBeanToPayload(),
                new CompetitionGroupIdsLocalToRemoteMerger()
        );
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetAddPayload, AddCompetitionGroupSetRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }

    private static class CompetitionGroupIdsLocalToRemoteMerger implements ObjectMerger<
            AddCompetitionGroupSetRequest,
            CompetitionGroupSetAddPayload> {

        @Override
        public void merge(AddCompetitionGroupSetRequest source, CompetitionGroupSetAddPayload destination) {
            ImmutableSet.Builder<String> competitionGroupIdsBuilder = ImmutableSet.builder();
            if (source.hasCompetitionGroups()) {
                for (AddCompetitionGroupSetRequest.CompetitionGroup competitionGroup : source.getCompetitionGroups()) {
                    competitionGroupIdsBuilder.add(competitionGroup.getId());
                }
            }
            destination.competitionGroupIds = competitionGroupIdsBuilder.build();
        }
    }
}
