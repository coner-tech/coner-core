package org.coner.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.hibernate.entity.HandicapGroupHibernateEntity;
import org.hibernate.SessionFactory;

import io.dropwizard.hibernate.AbstractDAO;

public class HandicapGroupDao
        extends AbstractDAO<HandicapGroupHibernateEntity>
        implements HibernateEntityDao<HandicapGroupHibernateEntity> {

    @Inject
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
