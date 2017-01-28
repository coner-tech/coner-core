package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupSetApiDomainBoundary extends AbstractBoundary<HandicapGroupSetApiEntity, HandicapGroupSet> {

    @Inject
    public HandicapGroupSetApiDomainBoundary() {
    }

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
