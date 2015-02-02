package org.coner.boundary;

import org.coner.core.domain.HandicapGroup;

/**
 *
 */
public class HandicapGroupBoundary extends AbstractBoundary<
        org.coner.api.entity.HandicapGroup,
        HandicapGroup,
        org.coner.hibernate.entity.HandicapGroup> {

    private static HandicapGroupBoundary instance;

    /**
     * Get the singleton instance of the HandicapGroupBoundary.
     *
     * @return an HandicapGroupBoundary
     */
    public static HandicapGroupBoundary getInstance() {
        if (instance == null) {
            instance = new HandicapGroupBoundary();
        }
        return instance;
    }

    public static void setInstance(HandicapGroupBoundary handicapGroupBoundary) {
        instance = handicapGroupBoundary;
    }

    /**
     * Package-private constructor which should only ever be called by `HandicapGroupBoundary.getInstance` or a test.
     */
    HandicapGroupBoundary() {
        super();
    }

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
