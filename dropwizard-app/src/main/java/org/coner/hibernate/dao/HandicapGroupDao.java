package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import java.util.List;
import org.hibernate.SessionFactory;

public class HandicapGroupDao extends AbstractDAO<HandicapGroupHibernateEntity> {

    public HandicapGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void create(HandicapGroupHibernateEntity handicapGroup) {
        persist(handicapGroup);
    }

    public List<HandicapGroupHibernateEntity> findAll() {
        return list(namedQuery(HandicapGroupHibernateEntity.QUERY_FIND_ALL));
    }

    public HandicapGroupHibernateEntity findById(String id) {
        return get(id);
    }
}
