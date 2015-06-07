package org.coner.boundary;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.core.domain.CompetitionGroup;

public class CompetitionGroupApiDomainBoundary extends AbstractBoundary<CompetitionGroupApiEntity, CompetitionGroup> {
    @Override
    protected EntityMerger<CompetitionGroupApiEntity, CompetitionGroup> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroup, CompetitionGroupApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
