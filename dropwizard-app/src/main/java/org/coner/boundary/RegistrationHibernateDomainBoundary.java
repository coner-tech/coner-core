package org.coner.boundary;

import org.coner.core.domain.Registration;
import org.coner.hibernate.entity.RegistrationHibernateEntity;

public class RegistrationHibernateDomainBoundary extends AbstractBoundary<RegistrationHibernateEntity, Registration> {

    private final EventHibernateDomainBoundary eventHibernateDomainBoundary;

    public RegistrationHibernateDomainBoundary(EventHibernateDomainBoundary eventHibernateDomainBoundary) {
        this.eventHibernateDomainBoundary = eventHibernateDomainBoundary;
    }

    @Override
    protected EntityMerger<RegistrationHibernateEntity, Registration> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setEvent(eventHibernateDomainBoundary.toRemoteEntity(source.getEvent()));
        });
    }

    @Override
    protected EntityMerger<Registration, RegistrationHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setEvent(eventHibernateDomainBoundary.toLocalEntity(source.getEvent()));
        });
    }
}
