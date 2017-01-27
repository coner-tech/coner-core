package org.coner.core.boundary;

import javax.inject.Inject;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.entity.Event;
import org.coner.core.util.merger.ObjectMerger;
import org.coner.core.util.merger.ReflectionJavaBeanMerger;

public class EventApiDomainBoundary extends AbstractBoundary<EventApiEntity, Event> {

    @Inject
    public EventApiDomainBoundary() {
    }

    @Override
    protected ObjectMerger<EventApiEntity, Event> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<Event, EventApiEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
