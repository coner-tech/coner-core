package org.coner.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.util.merger.UnsupportedOperationMerger;

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
