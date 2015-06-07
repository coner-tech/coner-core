package org.coner.boundary;

import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

public class CompetitionGroupSetHibernateDomainBoundary extends AbstractBoundary<
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSet> {

    @Override
    protected EntityMerger<CompetitionGroupSetHibernateEntity, CompetitionGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getCompetitionGroupSetId());
        });
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, CompetitionGroupSetHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setCompetitionGroupSetId(source.getId());
        });
    }
}
