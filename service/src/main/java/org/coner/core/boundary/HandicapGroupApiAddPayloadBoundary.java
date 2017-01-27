package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class HandicapGroupApiAddPayloadBoundary extends AbstractBoundary<
        AddHandicapGroupRequest,
        HandicapGroupAddPayload> {

    @Inject
    public HandicapGroupApiAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<AddHandicapGroupRequest, HandicapGroupAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<HandicapGroupAddPayload, AddHandicapGroupRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
