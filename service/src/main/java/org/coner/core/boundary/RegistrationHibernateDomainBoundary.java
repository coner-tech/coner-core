package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.entity.Registration;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.util.merger.CompositeMerger;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class RegistrationHibernateDomainBoundary extends AbstractBoundary<RegistrationHibernateEntity, Registration> {

    private final EventHibernateDomainBoundary eventHibernateDomainBoundary;

    @Inject
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
