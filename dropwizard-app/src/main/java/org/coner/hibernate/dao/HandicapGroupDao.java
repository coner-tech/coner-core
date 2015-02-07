package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.HandicapGroup;
import org.hibernate.SessionFactory;

/**
 *
 */
public class HandicapGroupDao extends AbstractDAO<HandicapGroup> {

    /**
     * Constructor for HandicapGroupDao.
     *
     * @param sessionFactory the `SessionFactory`
     */
    public HandicapGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Save or update the passed HandicapGroup in storage.
     *
     * @param handicapGroup the HandicapGroup to save or update
     */
    public void create(HandicapGroup handicapGroup) {
        persist(handicapGroup);
    }
}
