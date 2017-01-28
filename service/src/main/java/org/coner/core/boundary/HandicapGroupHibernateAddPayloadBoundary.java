package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class HandicapGroupHibernateAddPayloadBoundary extends AbstractBoundary<
        HandicapGroupHibernateEntity,
        HandicapGroupAddPayload> {

    @Inject
    public HandicapGroupHibernateAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<HandicapGroupHibernateEntity, HandicapGroupAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroupAddPayload, HandicapGroupHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
