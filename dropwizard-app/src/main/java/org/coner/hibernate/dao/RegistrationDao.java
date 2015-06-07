package org.coner.hibernate.dao;

import org.coner.hibernate.entity.*;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.*;

public class RegistrationDao
        extends AbstractDAO<RegistrationHibernateEntity>
        implements HibernateEntityDao<RegistrationHibernateEntity> {

    public RegistrationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public RegistrationHibernateEntity findById(String id) {
        return get(id);
    }

    public List<RegistrationHibernateEntity> getAllWith(EventHibernateEntity event) {
        Query query = namedQuery(RegistrationHibernateEntity.QUERY_FIND_ALL_WITH_EVENT);
        query.setParameter(RegistrationHibernateEntity.PARAMETER_EVENT_ID, event.getId());
        return list(query);
    }

    @Override
    public void create(RegistrationHibernateEntity registration) {
        persist(registration);
    }

    @Override
    public List<RegistrationHibernateEntity> findAll() {
        throw new UnsupportedOperationException();
    }
}
