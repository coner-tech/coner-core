package org.coner.boundary;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupBoundary extends AbstractBoundary<
        CompetitionGroupApiEntity,
        CompetitionGroup,
        CompetitionGroupHibernateEntity> {

    @Override
    protected EntityMerger<CompetitionGroupApiEntity, CompetitionGroup> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup, CompetitionGroupApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup,
            CompetitionGroupHibernateEntity> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupHibernateEntity,
            CompetitionGroup> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }
}
