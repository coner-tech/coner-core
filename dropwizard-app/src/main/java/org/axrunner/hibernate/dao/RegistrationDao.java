package org.axrunner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.axrunner.hibernate.entity.Event;
import org.axrunner.hibernate.entity.Registration;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 *
 */
public class RegistrationDao extends AbstractDAO<Registration> {
    public RegistrationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Registration> getAllWith(Event event) {
        Query query = namedQuery(Registration.QUERY_FIND_ALL_WITH_EVENT);
        query.setParameter(Registration.PARAMETER_EVENT_ID, event.getId());
        return list(query);
    }
}
