package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionPayloadJavaBeanMerger;
import org.coner.core.util.merger.UnsupportedOperationMerger;

public class CompetitionGroupSetHibernateAddPayloadBoundary extends AbstractBoundary<
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetAddPayload> {

    @Inject
    public CompetitionGroupSetHibernateAddPayloadBoundary() {
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetHibernateEntity, CompetitionGroupSetAddPayload>
    buildLocalToRemoteMerger() {
        return new UnsupportedOperationMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetAddPayload, CompetitionGroupSetHibernateEntity>
    buildRemoteToLocalMerger() {
        return ReflectionPayloadJavaBeanMerger.payloadToJavaBean();
    }
}
