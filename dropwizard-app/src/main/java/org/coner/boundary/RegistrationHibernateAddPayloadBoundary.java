package org.coner.boundary;

import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.hibernate.entity.RegistrationHibernateEntity;
import org.coner.util.merger.*;

public class RegistrationHibernateAddPayloadBoundary extends AbstractBoundary<
        RegistrationHibernateEntity,
        RegistrationAddPayload> {

    private final EventHibernateDomainBoundary eventHibernateDomainBoundary;

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
