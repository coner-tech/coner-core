package org.coner.boundary;

import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

public class HandicapGroupSetHibernateAddPayloadBoundary extends AbstractBoundary<
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetAddPayload> {
    @Override
    protected ObjectMerger<HandicapGroupSetHibernateEntity, HandicapGroupSetAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroupSetAddPayload, HandicapGroupSetHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
