package org.coner.boundary;

import org.coner.api.entity.EventApiEntity;
import org.coner.core.domain.Event;

public class EventApiDomainBoundary extends AbstractBoundary<EventApiEntity, Event> {
    @Override
    protected EntityMerger<EventApiEntity, Event> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, EventApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
