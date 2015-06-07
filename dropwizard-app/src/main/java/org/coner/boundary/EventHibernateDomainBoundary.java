package org.coner.boundary;

import org.coner.core.domain.Event;
import org.coner.hibernate.entity.EventHibernateEntity;

public class EventHibernateDomainBoundary extends AbstractBoundary<EventHibernateEntity, Event> {
    @Override
    protected EntityMerger<EventHibernateEntity, Event> buildLocalToRemoteMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, EventHibernateEntity> buildRemoteToLocalMerger() {
        return new ReflectionEntityMerger<>();
    }
}
