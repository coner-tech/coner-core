package org.coner.boundary;

import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.util.merger.*;

public class CompetitionGroupHibernateAddPayloadBoundary extends AbstractBoundary<
        CompetitionGroupHibernateEntity,
        CompetitionGroupAddPayload> {
    @Override
    protected ObjectMerger<CompetitionGroupHibernateEntity, CompetitionGroupAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroupAddPayload, CompetitionGroupHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}