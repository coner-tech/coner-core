package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import org.coner.boundary.CompetitionGroupSetBoundary;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.dao.CompetitionGroupSetDao;

/**
 * CompetitionGroupSetGateway wraps persistence layer interactions for CompetitionGroupSet domain entities.
 */
public class CompetitionGroupSetGateway {

    private final CompetitionGroupSetBoundary competitionGroupSetBoundary;
    private final CompetitionGroupSetDao competitionGroupSetDao;

    /**
     * Constructor for CompetitionGroupSetGateway
     *
     * @param competitionGroupSetBoundary the CompetitionGroupSetBoundary for converting domain CompetitionGroupSet
     *                                    entities to/from Hibernate entities
     * @param competitionGroupSetDao      the CompetitionGroupSetDao for interacting with the persistence  layer
     */
    public CompetitionGroupSetGateway(
            CompetitionGroupSetBoundary competitionGroupSetBoundary,
            CompetitionGroupSetDao competitionGroupSetDao
    ) {
        this.competitionGroupSetBoundary = competitionGroupSetBoundary;
        this.competitionGroupSetDao = competitionGroupSetDao;
    }

    /**
     * Create a new CompetitionGroupSet entity
     *
     * @param competitionGroupSet the CompetitionGroupSet entity to create
     */
    public void create(CompetitionGroupSet competitionGroupSet) {
        Preconditions.checkNotNull(competitionGroupSet);
        org.coner.hibernate.entity.CompetitionGroupSet hCompetitionGroupSet = competitionGroupSetBoundary
                .toHibernateEntity(competitionGroupSet);
        competitionGroupSetDao.createOrUpdate(hCompetitionGroupSet);
        competitionGroupSetBoundary.merge(hCompetitionGroupSet, competitionGroupSet);
    }
}
