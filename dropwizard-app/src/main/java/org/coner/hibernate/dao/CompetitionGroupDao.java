package org.coner.hibernate.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.coner.hibernate.entity.CompetitionGroup;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * CompetitionGroup-specific Hibernate Data Access Object.
 */
public class CompetitionGroupDao extends AbstractDAO<CompetitionGroup> {

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public CompetitionGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Save the passed CompetitionGroup in storage.
     *
     * @param competitionGroup the CompetitionGroup to save or update
     */
    public void createOrUpdate(CompetitionGroup competitionGroup) {
        persist(competitionGroup);
    }

    /**
     * Find all CompetitionGroup entities persisted in storage.
     *
     * @return a list of all CompetitionGroup entities
     */
    public List<CompetitionGroup> findAll() {
        return list(namedQuery(CompetitionGroup.QUERY_FIND_ALL));
    }


}
