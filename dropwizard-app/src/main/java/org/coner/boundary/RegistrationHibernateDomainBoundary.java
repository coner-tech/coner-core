package org.coner.boundary;

import org.coner.core.domain.entity.Registration;
import org.coner.hibernate.entity.RegistrationHibernateEntity;
import org.coner.util.merger.*;

public class RegistrationHibernateDomainBoundary extends AbstractBoundary<RegistrationHibernateEntity, Registration> {

    private final EventHibernateDomainBoundary eventHibernateDomainBoundary;

    public RegistrationHibernateDomainBoundary(EventHibernateDomainBoundary eventHibernateDomainBoundary) {
        this.eventHibernateDomainBoundary = eventHibernateDomainBoundary;
    }

    @Override
    protected ObjectMerger<RegistrationHibernateEntity, Registration> buildLocalToRemoteMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setEvent(eventHibernateDomainBoundary.toRemoteEntity(source.getEvent()));
                }
        );
    }

    @Override
    protected ObjectMerger<Registration, RegistrationHibernateEntity> buildRemoteToLocalMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setEvent(eventHibernateDomainBoundary.toLocalEntity(source.getEvent()));
                }
        );
    }
}
