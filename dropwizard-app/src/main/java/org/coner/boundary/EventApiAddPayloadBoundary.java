package org.coner.boundary;

import org.coner.api.request.AddEventRequest;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.util.merger.*;

public class EventApiAddPayloadBoundary extends AbstractBoundary<AddEventRequest, EventAddPayload> {
    @Override
    protected ObjectMerger<AddEventRequest, EventAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<EventAddPayload, AddEventRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
