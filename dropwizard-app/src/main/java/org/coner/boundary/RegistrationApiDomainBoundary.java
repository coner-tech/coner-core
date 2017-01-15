package org.coner.boundary;

import javax.inject.Inject;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.core.domain.entity.Registration;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

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
