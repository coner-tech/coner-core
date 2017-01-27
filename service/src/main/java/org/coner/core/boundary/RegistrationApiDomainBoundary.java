package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.entity.Registration;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class RegistrationApiDomainBoundary extends AbstractBoundary<RegistrationApiEntity, Registration> {

    @Inject
    public RegistrationApiDomainBoundary() {
    }

    @Override
    protected ObjectMerger<RegistrationApiEntity, Registration> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<Registration, RegistrationApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
