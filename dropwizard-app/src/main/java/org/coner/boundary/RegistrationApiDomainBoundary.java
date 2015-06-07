package org.coner.boundary;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.core.domain.entity.Registration;
import org.coner.util.merger.*;

public class RegistrationApiDomainBoundary extends AbstractBoundary<RegistrationApiEntity, Registration> {

    @Override
    protected ObjectMerger<RegistrationApiEntity, Registration> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<Registration, RegistrationApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
