package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class EventApiAddPayloadBoundary extends AbstractBoundary<AddEventRequest, EventAddPayload> {

    @Inject
    public EventApiAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<AddEventRequest, EventAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<EventAddPayload, AddEventRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
