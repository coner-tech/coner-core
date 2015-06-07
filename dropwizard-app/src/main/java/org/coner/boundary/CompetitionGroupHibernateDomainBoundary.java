package org.coner.boundary;

import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupHibernateDomainBoundary extends AbstractBoundary<
        CompetitionGroupHibernateEntity,
        CompetitionGroup> {
    @Override
    protected EntityMerger<CompetitionGroupHibernateEntity, CompetitionGroup> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup, CompetitionGroupHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
