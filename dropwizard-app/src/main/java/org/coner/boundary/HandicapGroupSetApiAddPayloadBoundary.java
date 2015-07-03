package org.coner.boundary;

import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.util.merger.*;

public class HandicapGroupSetApiAddPayloadBoundary extends AbstractBoundary<
        AddHandicapGroupSetRequest,
        HandicapGroupSetAddPayload> {
    @Override
    protected ObjectMerger<AddHandicapGroupSetRequest, HandicapGroupSetAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<HandicapGroupSetAddPayload, AddHandicapGroupSetRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
