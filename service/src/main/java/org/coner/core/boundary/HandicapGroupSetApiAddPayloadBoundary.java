package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class HandicapGroupSetApiAddPayloadBoundary extends AbstractBoundary<
        AddHandicapGroupSetRequest,
        HandicapGroupSetAddPayload> {

    @Inject
    public HandicapGroupSetApiAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<AddHandicapGroupSetRequest, HandicapGroupSetAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<HandicapGroupSetAddPayload, AddHandicapGroupSetRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
