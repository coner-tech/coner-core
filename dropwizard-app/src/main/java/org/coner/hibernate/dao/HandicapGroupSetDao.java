package org.coner.hibernate.dao;

import org.coner.hibernate.entity.HandicapGroupSet;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class HandicapGroupSetDao extends AbstractDAO<HandicapGroupSet> {

    public HandicapGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createOrUpdate(HandicapGroupSet handicapGroupSet) {
        persist(handicapGroupSet);
    }
}
