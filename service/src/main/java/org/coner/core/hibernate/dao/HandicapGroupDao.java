package org.coner.core.hibernate.dao;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.hibernate.SessionFactory;

public class HandicapGroupDao
        extends BaseHibernateEntityDao<HandicapGroupHibernateEntity> {

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
