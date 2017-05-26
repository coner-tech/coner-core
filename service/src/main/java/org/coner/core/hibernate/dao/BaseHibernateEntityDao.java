package org.coner.core.hibernate.dao;

import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public abstract class BaseHibernateEntityDao<E> extends AbstractDAO<E> implements HibernateEntityDao<E> {
    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public BaseHibernateEntityDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void save(E hibernateEntity) {
        currentSession().saveOrUpdate(hibernateEntity);
    }
}
