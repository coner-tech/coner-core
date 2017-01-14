package org.coner.boundary;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupHibernateDomainBoundary extends AbstractBoundary<
        CompetitionGroupHibernateEntity,
        CompetitionGroup> {
    @Override
    protected ObjectMerger<CompetitionGroupHibernateEntity, CompetitionGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroup, CompetitionGroupHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
