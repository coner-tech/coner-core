package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

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
