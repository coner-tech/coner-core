package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroup;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class CompetitionGroupApiDomainBoundary extends AbstractBoundary<CompetitionGroupApiEntity, CompetitionGroup> {

    @Inject
    public CompetitionGroupApiDomainBoundary() {
    }

    @Override
    protected ObjectMerger<CompetitionGroupApiEntity, CompetitionGroup> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<CompetitionGroup, CompetitionGroupApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
