package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class HandicapGroupDao
        extends AbstractDAO<HandicapGroupHibernateEntity>
        implements HibernateEntityDao<HandicapGroupHibernateEntity> {

    public HandicapGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(HandicapGroupHibernateEntity handicapGroup) {
        persist(handicapGroup);
    }

    @Override
    public List<HandicapGroupHibernateEntity> findAll() {
        return list(namedQuery(HandicapGroupHibernateEntity.QUERY_FIND_ALL));
    }

    @Override
    public HandicapGroupHibernateEntity findById(String id) {
        return get(id);
    }
}
