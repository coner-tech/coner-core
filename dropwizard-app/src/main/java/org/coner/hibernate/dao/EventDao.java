package org.coner.hibernate.dao;

import org.coner.hibernate.entity.EventHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class EventDao
        extends AbstractDAO<EventHibernateEntity>
        implements HibernateEntityDao<EventHibernateEntity> {

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
