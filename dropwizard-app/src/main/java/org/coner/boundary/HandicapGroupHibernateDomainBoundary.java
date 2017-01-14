package org.coner.boundary;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupHibernateDomainBoundary extends AbstractBoundary<
        HandicapGroupHibernateEntity,
        HandicapGroup> {
    @Override
    protected ObjectMerger<HandicapGroupHibernateEntity, HandicapGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroup, HandicapGroupHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
