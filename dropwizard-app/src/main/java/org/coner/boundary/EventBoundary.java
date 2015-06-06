package org.coner.boundary;

import org.coner.api.entity.EventApiEntity;
import org.coner.core.domain.Event;
import org.coner.hibernate.entity.EventHibernateEntity;

public class EventBoundary extends AbstractBoundary<EventApiEntity, Event, EventHibernateEntity> {

    @Override
    protected EntityMerger<EventApiEntity, Event> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, EventApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, EventHibernateEntity> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<EventHibernateEntity, Event> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }
}
