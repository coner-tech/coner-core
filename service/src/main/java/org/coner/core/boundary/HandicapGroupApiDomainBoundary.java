package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.entity.HandicapGroup;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class HandicapGroupApiDomainBoundary extends AbstractBoundary<HandicapGroupApiEntity, HandicapGroup> {

    @Inject
    public HandicapGroupApiDomainBoundary() {
    }

    @Override
    protected ObjectMerger<HandicapGroupApiEntity, HandicapGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<HandicapGroup, HandicapGroupApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
