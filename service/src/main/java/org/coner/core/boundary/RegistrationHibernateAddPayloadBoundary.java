package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.util.merger.CompositeMerger;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class RegistrationHibernateAddPayloadBoundary extends AbstractBoundary<
        RegistrationHibernateEntity,
        RegistrationAddPayload> {

    private final EventHibernateDomainBoundary eventHibernateDomainBoundary;

    @Inject
    public RegistrationHibernateAddPayloadBoundary(EventHibernateDomainBoundary eventHibernateDomainBoundary) {
        this.eventHibernateDomainBoundary = eventHibernateDomainBoundary;
    }

    @Override
    protected ObjectMerger<RegistrationHibernateEntity, RegistrationAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<RegistrationAddPayload, RegistrationHibernateEntity> buildRemoteToLocalMerger() {
        return new CompositeMerger<>(
                ReflectionPayloadJavaBeanMerger.payloadToJavaBean(),
                (source, destination) -> {
                    destination.setEvent(eventHibernateDomainBoundary.toLocalEntity(source.event));
                }
        );
    }
}
