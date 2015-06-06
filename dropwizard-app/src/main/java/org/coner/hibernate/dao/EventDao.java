package org.coner.hibernate.dao;

import org.coner.hibernate.entity.Event;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

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
