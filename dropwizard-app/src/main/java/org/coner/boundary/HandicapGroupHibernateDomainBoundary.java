package org.coner.boundary;

import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

public class HandicapGroupHibernateDomainBoundary extends AbstractBoundary<
        HandicapGroupHibernateEntity,
        HandicapGroup> {
    @Override
    protected EntityMerger<HandicapGroupHibernateEntity, HandicapGroup> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, HandicapGroupHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
