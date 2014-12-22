package org.axrunner.hibernate.gateway;

import io.dropwizard.hibernate.HibernateBundle;
import org.axrunner.AxRunnerDropwizardConfiguration;
import org.axrunner.boundary.EventBoundary;
import org.axrunner.core.domain.Event;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 *
 */
public class EventGateway extends HibernateGateway {

    private final EventBoundary eventBoundary;

    public EventGateway(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate, EventBoundary eventBoundary) {
        super(hibernate);
        this.eventBoundary = eventBoundary;
    }

    public List<Event> getAll() {
        Session session = getHibernate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery("from Event");
            List<org.axrunner.hibernate.entity.Event> events = query.list();
            return eventBoundary.toDomainEntities(events);
        } finally {
            session.close();
        }
    }

    public Event findById(String eventId) {
        return null;
    }

    public void create(org.axrunner.core.domain.Event event) {
        org.axrunner.hibernate.entity.Event hibernateEvent = eventBoundary.toHibernateEntity(event);
        Session session = getHibernate().getSessionFactory().openSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(hibernateEvent);
            transaction.commit();
            eventBoundary.merge(hibernateEvent, event);
        } finally {
            session.close();
        }
    }
}
