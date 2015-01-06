package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.Event;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Event-specific Hibernate Data Access Object
 */
public class EventDao extends AbstractDAO<Event> {

    /**
     * Constructor for EventDao
     *
     * @param sessionFactory the `SessionFactory`
     */
    public EventDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find an Event by id
     *
     * @param id the id of the Event to find
     * @return the Event having `id` or null if not found
     */
    public Event findById(String id) {
        return get(id);
    }

    /**
     * Find all Event entities persisted in storage
     *
     * @return a list of all Event entities
     */
    public List<Event> findAll() {
        return list(namedQuery(Event.QUERY_FIND_ALL));
    }

    /**
     * Save or update the passed Event in storage
     *
     * @param event the Event to save or update
     */
    public void create(Event event) {
        persist(event);
    }
}
