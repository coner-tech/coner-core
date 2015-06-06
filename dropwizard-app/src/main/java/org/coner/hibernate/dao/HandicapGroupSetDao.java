package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class HandicapGroupSetDao extends AbstractDAO<HandicapGroupSetHibernateEntity> {

    public HandicapGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(HandicapGroupSetHibernateEntity handicapGroupSet) {
        persist(handicapGroupSet);
    }
}
