package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.CompetitionGroupSet;
import org.hibernate.SessionFactory;

/**
 * CompetitionGroupSet-specific Hibernate Data Access Object.
 */
public class CompetitionGroupSetDao extends AbstractDAO<CompetitionGroupSet> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public CompetitionGroupSetDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Save the passed CompetitionGroupSet in storage.
     *
     * @param competitionGroupSet the CompetitionGroupSet to save or update
     */
    public void createOrUpdate(CompetitionGroupSet competitionGroupSet) {
        persist(competitionGroupSet);
    }
}
