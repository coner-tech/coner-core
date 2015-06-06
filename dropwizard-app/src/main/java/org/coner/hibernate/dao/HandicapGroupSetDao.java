package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.HandicapGroupSet;
import org.hibernate.SessionFactory;

/**
 * HandicapGroupSet-specific Hibernate Data Access Object.
 */
public class HandicapGroupSetDao extends AbstractDAO<HandicapGroupSet> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public HandicapGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Save the passed HandicapGroupSet in storage.
     *
     * @param handicapGroupSet the HandicapGroupSet to save or update
     */
    public void createOrUpdate(HandicapGroupSet handicapGroupSet) {
        persist(handicapGroupSet);
    }
}
