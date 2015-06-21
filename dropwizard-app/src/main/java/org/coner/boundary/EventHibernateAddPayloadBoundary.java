package org.coner.boundary;

import org.coner.core.domain.payload.EventAddPayload;
import org.coner.hibernate.entity.EventHibernateEntity;
import org.coner.util.merger.*;

public class EventHibernateAddPayloadBoundary extends AbstractBoundary<EventHibernateEntity, EventAddPayload> {
    @Override
    protected ObjectMerger<EventHibernateEntity, EventAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<EventAddPayload, EventHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
