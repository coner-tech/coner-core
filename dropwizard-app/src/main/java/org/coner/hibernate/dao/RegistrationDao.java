package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.Event;
import org.coner.hibernate.entity.Registration;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Registration-specific Hibernate Data Access Object.
 */
public class RegistrationDao extends AbstractDAO<Registration> {

    /**
     * Constructor for RegistrationDao.
     *
     * @param sessionFactory the `SessionFactory`
     */
    public RegistrationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Find a Registration by id.
     *
     * @param id the id of the Registration to find
     * @return the Registration having `id` or null if not found
     */
    public Registration findById(String id) {
        return get(id);
    }

    /**
     * Find all Registration entities persisted in storage for the Event.
     *
     * @param event the Event of all Registrations to search
     * @return a list of all Registration entities
     */
    public List<Registration> getAllWith(Event event) {
        Query query = namedQuery(Registration.QUERY_FIND_ALL_WITH_EVENT);
        query.setParameter(Registration.PARAMETER_EVENT_ID, event.getId());
        return list(query);
    }

    /**
     * Save or update the passed Registration in storage.
     *
     * @param registration the Registration to save or update
     */
    public void create(Registration registration) {
        persist(registration);
    }
}
