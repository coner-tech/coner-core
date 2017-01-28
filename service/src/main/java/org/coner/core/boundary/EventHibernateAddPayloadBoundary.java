package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class EventHibernateAddPayloadBoundary extends AbstractBoundary<EventHibernateEntity, EventAddPayload> {

    @Inject
    public EventHibernateAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<EventHibernateEntity, EventAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<EventAddPayload, EventHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
