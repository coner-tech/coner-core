package org.coner.boundary;

import org.coner.api.entity.HandicapGroupSetApiEntity;
import org.coner.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupSetApiDomainBoundary extends AbstractBoundary<HandicapGroupSetApiEntity, HandicapGroupSet> {

    private ObjectMerger<AddHandicapGroupSetRequest, HandicapGroupSet>
            addHandicapGroupSetRequestHandicapGroupSetEntityMerger;

    public HandicapGroupSet toRemoteEntity(AddHandicapGroupSetRequest addHandicapGroupSetRequest) {
        if (addHandicapGroupSetRequestHandicapGroupSetEntityMerger == null) {
            addHandicapGroupSetRequestHandicapGroupSetEntityMerger = new ReflectionJavaBeanMerger<>();
        }
        HandicapGroupSet handicapGroupSet = new HandicapGroupSet();
        addHandicapGroupSetRequestHandicapGroupSetEntityMerger.merge(addHandicapGroupSetRequest, handicapGroupSet);
        return handicapGroupSet;
    }

    @Override
    protected ObjectMerger<HandicapGroupSetApiEntity, HandicapGroupSet> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroupSet, HandicapGroupSetApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
