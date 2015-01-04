package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.boundary.EventBoundary;
import org.coner.core.domain.Event;
import org.coner.hibernate.dao.EventDao;

import java.util.List;

/**
 * EventGateway wraps persistence layer interactions for Event domain entities.
 */
public class EventGateway {

    private final EventBoundary eventBoundary;
    private final EventDao eventDao;

    /**
     * Constructor for EventGateway.
     *
     * @param eventBoundary the EventBoundary for converting Domain entities to/from Hibernate entities
     * @param eventDao      the EventDao for interacting with the persistence layer
     */
    public EventGateway(EventBoundary eventBoundary, EventDao eventDao) {
        this.eventBoundary = eventBoundary;
        this.eventDao = eventDao;
    }

    /**
     * Get all Event entities.
     *
     * @return a list of all Event entities
     */
    public List<Event> getAll() {
        List<org.coner.hibernate.entity.Event> events = eventDao.findAll();
        return eventBoundary.toDomainEntities(events);
    }

    /**
     * Get an Event entity by id.
     *
     * @param eventId eventId of the Event
     * @return the Event entity with id or null if not found
     */
    public Event findById(String eventId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(eventId));
        org.coner.hibernate.entity.Event hibernateEvent = eventDao.findById(eventId);
        return eventBoundary.toDomainEntity(hibernateEvent);
    }

    /**
     * Persist a new Event entity.
     *
     * @param event the Event entity to persist
     */
    public void create(Event event) {
        Preconditions.checkNotNull(event);
        org.coner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        eventDao.create(hibernateEvent);
        eventBoundary.merge(hibernateEvent, event);
    }
}
