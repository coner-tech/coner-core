package org.coner.boundary;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.HandicapGroupSet;

public class HandicapGroupSetApiDomainBoundary extends AbstractBoundary<HandicapGroupSetApiEntity, HandicapGroupSet> {

    private EntityMerger<AddHandicapGroupSetRequest, HandicapGroupSet>
            addHandicapGroupSetRequestHandicapGroupSetEntityMerger;

    public HandicapGroupSet toRemoteEntity(AddHandicapGroupSetRequest addHandicapGroupSetRequest) {
        if (addHandicapGroupSetRequestHandicapGroupSetEntityMerger == null) {
            addHandicapGroupSetRequestHandicapGroupSetEntityMerger = new ReflectionEntityMerger<>();
        }
        HandicapGroupSet handicapGroupSet = new HandicapGroupSet();
        addHandicapGroupSetRequestHandicapGroupSetEntityMerger.merge(addHandicapGroupSetRequest, handicapGroupSet);
        return handicapGroupSet;
    }

    @Override
    protected EntityMerger<HandicapGroupSetApiEntity, HandicapGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroupSet, HandicapGroupSetApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
