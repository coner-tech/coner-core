package org.coner.core.gateway;

import org.coner.boundary.EventBoundary;
import org.coner.core.domain.Event;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.entity.EventHibernateEntity;

import com.google.common.base.*;
import java.util.List;

public class EventGateway {

    private final EventBoundary eventBoundary;
    private final EventDao eventDao;

    public EventGateway(EventBoundary eventBoundary, EventDao eventDao) {
        this.eventBoundary = eventBoundary;
        this.eventDao = eventDao;
    }

    public List<Event> getAll() {
        List<EventHibernateEntity> events = eventDao.findAll();
        return eventBoundary.toDomainEntities(events);
    }

    public Event findById(String eventId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(eventId));
        EventHibernateEntity hibernateEvent = eventDao.findById(eventId);
        return eventBoundary.toDomainEntity(hibernateEvent);
    }

    public void create(Event event) {
        Preconditions.checkNotNull(event);
        EventHibernateEntity hibernateEvent = eventBoundary.toHibernateEntity(event);
        eventDao.create(hibernateEvent);
        eventBoundary.merge(hibernateEvent, event);
    }
}
