package org.axrunner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.axrunner.hibernate.entity.Event;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 *
 */
public class EventDao extends AbstractDAO<Event> {
    public EventDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Event findById(String id) {
        return get(id);
    }

    public List<Event> findAll() {
        return list(namedQuery(Event.QUERY_FIND_ALL));
    }

    public void create(Event event) {
        persist(event);
    }
}
