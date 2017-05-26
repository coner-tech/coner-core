package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class RegistrationDao
        extends BaseHibernateEntityDao<RegistrationHibernateEntity> {

    @Inject
    public RegistrationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public RegistrationHibernateEntity findById(String id) {
        return get(id);
    }

    public List<RegistrationHibernateEntity> getAllWith(EventHibernateEntity event) {
        Query<RegistrationHibernateEntity> query = namedQuery(RegistrationHibernateEntity.QUERY_FIND_ALL_WITH_EVENT);
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
