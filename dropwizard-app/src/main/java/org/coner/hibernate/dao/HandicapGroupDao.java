package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.HandicapGroup;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * HandicapGroup-specific Hibernate Data Access Object.
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

    /**
     * Find all HandicapGroup entities persisted in storage.
     *
     * @return a list of all HandicapGroup entities
     */
    public List<HandicapGroup> findAll() {
        return list(namedQuery(HandicapGroup.QUERY_FIND_ALL));
    }
}
