package org.axrunner.hibernate.gateway;

import io.dropwizard.hibernate.HibernateBundle;
import org.axrunner.AxRunnerDropwizardConfiguration;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.domain.Event;
import org.axrunner.hibernate.dao.EventDao;

import java.util.List;

/**
 *
 */
public class EventGateway extends HibernateGateway {

    private final EventBoundary eventBoundary;
    private final EventDao eventDao;

    public EventGateway(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate, EventBoundary eventBoundary, EventDao eventDao) {
        super(hibernate);
        this.eventBoundary = eventBoundary;
        this.eventDao = eventDao;
    }

    public List<Event> getAll() {
        List<org.axrunner.hibernate.entity.Event> events = eventDao.findAll();
        return eventBoundary.toDomainEntities(events);
    }

    public Event findById(String eventId) {
        org.axrunner.hibernate.entity.Event hibernateEvent = eventDao.findById(eventId);
        return eventBoundary.toDomainEntity(hibernateEvent);
    }

    public void create(org.axrunner.core.domain.Event event) {
        org.axrunner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        eventDao.create(hibernateEvent);
        eventBoundary.merge(hibernateEvent, event);
    }
}
