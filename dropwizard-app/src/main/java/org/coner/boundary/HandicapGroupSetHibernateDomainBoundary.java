package org.coner.boundary;

import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

public class HandicapGroupSetHibernateDomainBoundary extends AbstractBoundary<
        HandicapGroupSetHibernateEntity,
        HandicapGroupSet> {
    @Override
    protected EntityMerger<HandicapGroupSetHibernateEntity, HandicapGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getHandicapGroupSetId());
        });
    }

    @Override
    protected EntityMerger<HandicapGroupSet, HandicapGroupSetHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setHandicapGroupSetId(source.getId());
        });
    }
}
