package org.axrunner.hibernate.gateway;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.domain.Event;
import org.axrunner.hibernate.dao.EventDao;

import java.util.List;

/**
 *
 */
public class EventGateway {

    private final EventBoundary eventBoundary;
    private final EventDao eventDao;

    public EventGateway(EventBoundary eventBoundary, EventDao eventDao) {
        this.eventBoundary = eventBoundary;
        this.eventDao = eventDao;
    }

    public List<Event> getAll() {
        List<org.axrunner.hibernate.entity.Event> events = eventDao.findAll();
        return eventBoundary.toDomainEntities(events);
    }

    public Event findById(String eventId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(eventId));
        org.axrunner.hibernate.entity.Event hibernateEvent = eventDao.findById(eventId);
        return eventBoundary.toDomainEntity(hibernateEvent);
    }

    public void create(org.axrunner.core.domain.Event event) {
        Preconditions.checkNotNull(event);
        org.axrunner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        eventDao.create(hibernateEvent);
        eventBoundary.merge(hibernateEvent, event);
    }
}
