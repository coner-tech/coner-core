package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;

import java.util.List;

/**
 * CompetitionGroupGateway wraps persistence layer interactions for CompetitionGroup domain entities.
 */
public class CompetitionGroupGateway {

    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final CompetitionGroupDao competitionGroupDao;

    /**
     * Constructor for CompetitionGroupGateway.
     *
     * @param competitionGroupBoundary the CompetitionGroupBoundary for converting Domain entities to/from Hibernate
     *                                 entities
     * @param competitionGroupDao      the CompetitionGroupDao for interacting with the persistence layer
     */
    public CompetitionGroupGateway(
            CompetitionGroupBoundary competitionGroupBoundary,
            CompetitionGroupDao competitionGroupDao
    ) {
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.competitionGroupDao = competitionGroupDao;
    }

    /**
     * Create a new CompetitionGroup entity.
     *
     * @param competitionGroup the CompetitionGroup to create
     */
    public void create(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        org.coner.hibernate.entity.CompetitionGroup hCompetitionGroup = competitionGroupBoundary.toHibernateEntity(
                competitionGroup
        );
        competitionGroupDao.createOrUpdate(hCompetitionGroup);
        competitionGroupBoundary.merge(hCompetitionGroup, competitionGroup);
    }

    /**
     * Get all CompetitionGroup entities.
     *
     * @return list of CompetitionGroup entities
     */
    public List<CompetitionGroup> getAll() {
        List<org.coner.hibernate.entity.CompetitionGroup> competitionGroups = competitionGroupDao.findAll();
        return competitionGroupBoundary.toDomainEntities(competitionGroups);
    }

    /**
     * Get a CompetitionGroup entity by id.
     *
     * @param competitionGroupId id of the CompetitionGroup
     * @return the CompetitionGroup entity with id or null if not found
     */
    public CompetitionGroup findById(String competitionGroupId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(competitionGroupId));
        org.coner.hibernate.entity.CompetitionGroup hibernateCompetitionGroup = competitionGroupDao.findById(
                competitionGroupId
        );
        return competitionGroupBoundary.toDomainEntity(hibernateCompetitionGroup);
    }
}
