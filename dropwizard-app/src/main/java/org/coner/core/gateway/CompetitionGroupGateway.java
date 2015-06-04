package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;

import com.google.common.base.*;
import java.util.List;

public class CompetitionGroupGateway {

    private final CompetitionGroupBoundary competitionGroupBoundary;
    private final CompetitionGroupDao competitionGroupDao;

    public CompetitionGroupGateway(
            CompetitionGroupBoundary competitionGroupBoundary,
            CompetitionGroupDao competitionGroupDao
    ) {
        this.competitionGroupBoundary = competitionGroupBoundary;
        this.competitionGroupDao = competitionGroupDao;
    }

    public void create(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        org.coner.hibernate.entity.CompetitionGroup hCompetitionGroup = competitionGroupBoundary.toHibernateEntity(
                competitionGroup
        );
        competitionGroupDao.createOrUpdate(hCompetitionGroup);
        competitionGroupBoundary.merge(hCompetitionGroup, competitionGroup);
    }

    public List<CompetitionGroup> getAll() {
        List<org.coner.hibernate.entity.CompetitionGroup> competitionGroups = competitionGroupDao.findAll();
        return competitionGroupBoundary.toDomainEntities(competitionGroups);
    }

    public CompetitionGroup findById(String competitionGroupId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(competitionGroupId));
        org.coner.hibernate.entity.CompetitionGroup hibernateCompetitionGroup = competitionGroupDao.findById(
                competitionGroupId
        );
        return competitionGroupBoundary.toDomainEntity(hibernateCompetitionGroup);
    }
}
