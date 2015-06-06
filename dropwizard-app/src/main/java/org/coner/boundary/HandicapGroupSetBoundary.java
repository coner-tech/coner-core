package org.coner.boundary;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

public class HandicapGroupSetBoundary extends AbstractBoundary<
        HandicapGroupSetApiEntity,
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity> {

    private EntityMerger<AddHandicapGroupSetRequest, HandicapGroupSet>
            apiAddHandicapGroupSetRequestToDomainHandicapGroupSetEntityMerger;

    @Override
    protected EntityMerger<HandicapGroupSetApiEntity, HandicapGroupSet> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

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
    protected EntityMerger<HandicapGroupSet, HandicapGroupSetApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroupSet, HandicapGroupSetHibernateEntity>
    buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setHandicapGroupSetId(source.getId());
        });
    }

    @Override
    protected EntityMerger<HandicapGroupSetHibernateEntity, HandicapGroupSet>
    buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getHandicapGroupSetId());
        });
    }
}
