package org.coner.boundary;

import org.coner.api.entity.EventApiEntity;
import org.coner.core.domain.entity.Event;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class EventApiDomainBoundary extends AbstractBoundary<EventApiEntity, Event> {
    @Override
    protected ObjectMerger<EventApiEntity, Event> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<Event, EventApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
