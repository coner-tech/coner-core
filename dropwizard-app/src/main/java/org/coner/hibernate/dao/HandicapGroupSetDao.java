package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class HandicapGroupSetDao
        extends AbstractDAO<HandicapGroupSetHibernateEntity>
        implements HibernateEntityDao<HandicapGroupSetHibernateEntity> {

    public HandicapGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void create(HandicapGroupSetHibernateEntity entity) {
        persist(entity);
    }

    @Override
    public List<HandicapGroupSetHibernateEntity> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HandicapGroupSetHibernateEntity findById(String id) {
        throw new UnsupportedOperationException();
    }
}
