package org.coner.boundary;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.hibernate.entity.EventHibernateEntity;
import org.coner.util.merger.ObjectMerger;
import org.coner.util.merger.ReflectionJavaBeanMerger;

public class EventHibernateDomainBoundary extends AbstractBoundary<EventHibernateEntity, Event> {

    @Inject
    public EventHibernateDomainBoundary() {
    }

    @Override
    protected ObjectMerger<EventHibernateEntity, Event> buildLocalToRemoteMerger() {
        return new ReflectionJavaBeanMerger<>();
    }

    @Override
    protected ObjectMerger<Event, EventHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionJavaBeanMerger<>();
    }
}
