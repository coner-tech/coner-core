package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;

/**
 * CompetitionGroupGateway wraps persistence layer interactions for CompetitionGroup domain entities.
 */
public class CompetitionGroupGateway {

    private final CompetitionGroupBoundary boundary;
    private final CompetitionGroupDao dao;

    public CompetitionGroupGateway(CompetitionGroupBoundary boundary, CompetitionGroupDao dao) {
        this.boundary = boundary;
        this.dao = dao;
    }

    /**
     * Create a new CompetitionGroup entity
     *
     * @param competitionGroup the CompetitionGroup to create
     */
    public void create(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        org.coner.hibernate.entity.CompetitionGroup hCompetitionGroup = boundary.toHibernateEntity(competitionGroup);
        dao.createOrUpdate(hCompetitionGroup);
        boundary.merge(hCompetitionGroup, competitionGroup);
    }
}
