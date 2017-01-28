package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class EventDao
        extends AbstractDAO<EventHibernateEntity>
        implements HibernateEntityDao<EventHibernateEntity> {

    @Inject
    public EventDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public EventHibernateEntity findById(String id) {
        return get(id);
    }

    @Override
    public List<EventHibernateEntity> findAll() {
        return list(namedQuery(EventHibernateEntity.QUERY_FIND_ALL));
    }

    @Override
    public void create(EventHibernateEntity event) {
        persist(event);
    }
}
