package org.coner.hibernate.dao;

import org.coner.hibernate.entity.*;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.*;

public class RegistrationDao extends AbstractDAO<Registration> {

    public RegistrationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Registration findById(String id) {
        return get(id);
    }

    public List<Registration> getAllWith(Event event) {
        Query query = namedQuery(Registration.QUERY_FIND_ALL_WITH_EVENT);
        query.setParameter(Registration.PARAMETER_EVENT_ID, event.getId());
        return list(query);
    }

    public void create(Registration registration) {
        persist(registration);
    }
}
