package org.coner.boundary;

import javax.inject.Inject;

import org.coner.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

public class CompetitionGroupApiAddPayloadBoundary extends AbstractBoundary<
        AddCompetitionGroupRequest,
        CompetitionGroupAddPayload> {

    @Inject
    public CompetitionGroupApiAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<AddCompetitionGroupRequest, CompetitionGroupAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<CompetitionGroupAddPayload, AddCompetitionGroupRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
