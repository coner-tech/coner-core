package org.coner.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

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
