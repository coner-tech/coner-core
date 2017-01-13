package org.coner.boundary;

import org.coner.api.request.AddRegistrationRequest;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

public class RegistrationApiAddPayloadBoundary extends AbstractBoundary<
        AddRegistrationRequest,
        RegistrationAddPayload> {
    @Override
    protected ObjectMerger<AddRegistrationRequest, RegistrationAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<RegistrationAddPayload, AddRegistrationRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
