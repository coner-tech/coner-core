package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.util.merger.CompositeMerger;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupSetHibernateDomainBoundary extends AbstractBoundary<
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSet> {

    @Inject
    public CompetitionGroupSetHibernateDomainBoundary() {
    }

    @Override
    protected ObjectMerger<CompetitionGroupSetHibernateEntity, CompetitionGroupSet> buildLocalToRemoteMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setId(source.getCompetitionGroupSetId());
                }
        );
    }

    @Override
    protected ObjectMerger<CompetitionGroupSet, CompetitionGroupSetHibernateEntity> buildRemoteToLocalMerger() {
        return new CompositeMerger<>(
                new ReflectionJavaBeanMerger<>(),
                (source, destination) -> {
                    destination.setCompetitionGroupSetId(source.getId());
                }
        );
    }
}
