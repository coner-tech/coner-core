package org.coner.boundary;

import javax.inject.Inject;

import org.coner.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

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
