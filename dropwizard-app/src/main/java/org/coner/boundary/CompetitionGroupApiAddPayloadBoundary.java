package org.coner.boundary;

import org.coner.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.util.merger.*;

public class CompetitionGroupApiAddPayloadBoundary extends AbstractBoundary<
        AddCompetitionGroupRequest,
        CompetitionGroupAddPayload> {
    @Override
    protected ObjectMerger<AddCompetitionGroupRequest, CompetitionGroupAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<CompetitionGroupAddPayload, AddCompetitionGroupRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
