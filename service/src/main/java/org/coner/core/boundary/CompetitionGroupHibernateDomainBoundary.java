package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.CompetitionGroup;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupHibernateDomainBoundary extends AbstractBoundary<
        CompetitionGroupHibernateEntity,
        CompetitionGroup> {

    @Inject
    public CompetitionGroupHibernateDomainBoundary() {
    }

    @Override
    protected ObjectMerger<CompetitionGroupHibernateEntity, CompetitionGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroup, CompetitionGroupHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
