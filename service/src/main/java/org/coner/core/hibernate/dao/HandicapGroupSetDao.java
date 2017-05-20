package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class HandicapGroupSetDao
        extends AbstractDAO<HandicapGroupSetHibernateEntity>
        implements HibernateEntityDao<HandicapGroupSetHibernateEntity> {

    @Inject
    public HandicapGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(HandicapGroupSetHibernateEntity entity) {
        persist(entity);
    }

    @Override
    public List<HandicapGroupSetHibernateEntity> findAll() {
        return list(namedQuery(HandicapGroupSetHibernateEntity.QUERY_FIND_ALL));
    }

    @Override
    public HandicapGroupSetHibernateEntity findById(String id) {
        throw new UnsupportedOperationException();
    }
}
