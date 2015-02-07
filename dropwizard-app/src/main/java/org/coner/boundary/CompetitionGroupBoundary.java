package org.coner.boundary;

import org.coner.core.domain.CompetitionGroup;

/**
 *
 */
public class CompetitionGroupBoundary extends AbstractBoundary<
        org.coner.api.entity.CompetitionGroup,
        CompetitionGroup,
        org.coner.hibernate.entity.CompetitionGroup> {

    @Override
    protected EntityMerger<org.coner.api.entity.CompetitionGroup, CompetitionGroup> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup, org.coner.api.entity.CompetitionGroup> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup, org.coner.hibernate.entity.CompetitionGroup> buildDomainToHibernateMerger
            () {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.CompetitionGroup, CompetitionGroup> buildHibernateToDomainMerger
            () {
        return new ReflectionEntityMerger<>();
    }
}
