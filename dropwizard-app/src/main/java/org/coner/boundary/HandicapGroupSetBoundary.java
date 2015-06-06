package org.coner.boundary;

import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.HandicapGroupSet;

/**
 * Converts HandicapGroupSet entities as they cross architectural boundaries
 */
public class HandicapGroupSetBoundary extends AbstractBoundary<
        org.coner.api.entity.HandicapGroupSet,
        HandicapGroupSet,
        org.coner.hibernate.entity.HandicapGroupSet> {

    private EntityMerger<org.coner.api.request.AddHandicapGroupSetRequest, HandicapGroupSet>
            apiAddHandicapGroupSetRequestToDomainHandicapGroupSetEntityMerger;

    @Override
    protected EntityMerger<org.coner.api.entity.HandicapGroupSet, HandicapGroupSet> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    /**
     * Convert an API AddHandicapGroupSetRequest to a domain HandicapGroupSet
     *
     * @param addHandicapGroupSetRequest API request to convert
     * @return a HandicapGroupSet entity
     */
    public HandicapGroupSet toDomainEntity(AddHandicapGroupSetRequest addHandicapGroupSetRequest) {
        if (apiAddHandicapGroupSetRequestToDomainHandicapGroupSetEntityMerger == null) {
            apiAddHandicapGroupSetRequestToDomainHandicapGroupSetEntityMerger = new ReflectionEntityMerger<>();
        }
        HandicapGroupSet handicapGroupSet = new HandicapGroupSet();
        apiAddHandicapGroupSetRequestToDomainHandicapGroupSetEntityMerger.merge(
                addHandicapGroupSetRequest,
                handicapGroupSet
        );
        return handicapGroupSet;
    }

    @Override
    protected EntityMerger<HandicapGroupSet, org.coner.api.entity.HandicapGroupSet> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroupSet, org.coner.hibernate.entity.HandicapGroupSet>
    buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setHandicapGroupSetId(source.getId());
        });
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.HandicapGroupSet, HandicapGroupSet>
    buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getHandicapGroupSetId());
        });
    }
}
