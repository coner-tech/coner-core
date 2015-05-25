package org.coner.boundary;

import org.coner.core.domain.CompetitionGroupSet;

/**
 * Converts CompetitionGroupSet entities as they cross architectural boundaries
 */
public class CompetitionGroupSetBoundary extends AbstractBoundary<
        org.coner.api.entity.CompetitionGroupSet,
        CompetitionGroupSet,
        org.coner.hibernate.entity.CompetitionGroupSet
        > {
    @Override
    protected EntityMerger<org.coner.api.entity.CompetitionGroupSet, CompetitionGroupSet> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, org.coner.api.entity.CompetitionGroupSet> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, org.coner.hibernate.entity.CompetitionGroupSet>
    buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setCompetitionGroupSetId(source.getId());
        });
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.CompetitionGroupSet, CompetitionGroupSet>
    buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getCompetitionGroupSetId());
        });
    }
}
