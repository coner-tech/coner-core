package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class CompetitionGroupHibernateAddPayloadBoundary extends AbstractBoundary<
        CompetitionGroupHibernateEntity,
        CompetitionGroupAddPayload> {

    @Inject
    public CompetitionGroupHibernateAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<CompetitionGroupHibernateEntity, CompetitionGroupAddPayload> buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroupAddPayload, CompetitionGroupHibernateEntity> buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
