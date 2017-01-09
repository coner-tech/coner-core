package org.coner.boundary;

import org.coner.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.util.merger.*;

public class HandicapGroupApiAddPayloadBoundary extends AbstractBoundary<
        AddHandicapGroupRequest,
        HandicapGroupAddPayload> {
    @Override
    protected ObjectMerger<AddHandicapGroupRequest, HandicapGroupAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<HandicapGroupAddPayload, AddHandicapGroupRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}