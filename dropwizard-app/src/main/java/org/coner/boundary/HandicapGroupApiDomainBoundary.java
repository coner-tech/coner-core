package org.coner.boundary;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.core.domain.HandicapGroup;

public class HandicapGroupApiDomainBoundary extends AbstractBoundary<HandicapGroupApiEntity, HandicapGroup> {
    @Override
    protected EntityMerger<HandicapGroupApiEntity, HandicapGroup> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, HandicapGroupApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
