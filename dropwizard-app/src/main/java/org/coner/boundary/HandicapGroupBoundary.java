package org.coner.boundary;

import org.coner.core.domain.HandicapGroup;

/**
 * Converts HandicapGroup entities as they cross architectural boundaries.
 */
public class HandicapGroupBoundary extends AbstractBoundary<
        org.coner.api.entity.HandicapGroup,
        HandicapGroup,
        org.coner.hibernate.entity.HandicapGroup> {

    @Override
    protected EntityMerger<org.coner.api.entity.HandicapGroup, HandicapGroup> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, org.coner.api.entity.HandicapGroup> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, org.coner.hibernate.entity.HandicapGroup> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.HandicapGroup, HandicapGroup> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }
}
