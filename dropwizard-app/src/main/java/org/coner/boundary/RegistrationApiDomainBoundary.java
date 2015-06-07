package org.coner.boundary;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.core.domain.Registration;

public class RegistrationApiDomainBoundary extends AbstractBoundary<RegistrationApiEntity, Registration> {

    @Override
    protected EntityMerger<RegistrationApiEntity, Registration> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Registration, RegistrationApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
