package org.coner.boundary;

import javax.inject.Inject;

import org.coner.api.entity.CompetitionGroupApiEntity;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

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
