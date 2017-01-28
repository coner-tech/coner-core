package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class RegistrationApiAddPayloadBoundary extends AbstractBoundary<
        AddRegistrationRequest,
        RegistrationAddPayload> {

    @Inject
    public RegistrationApiAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<AddRegistrationRequest, RegistrationAddPayload> buildLocalToRemoteMerger() {
        return ReflectionPayloadJavaBeanMerger.javaBeanToPayload();
    }

    @Override
    protected ObjectMerger<RegistrationAddPayload, AddRegistrationRequest> buildRemoteToLocalMerger() {
        return new UnsupportedOperationMerger<>();
    }
}
