package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupSetBoundary;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.dao.CompetitionGroupSetDao;

import com.google.common.base.Preconditions;

public class CompetitionGroupSetGateway {

    private final CompetitionGroupSetBoundary competitionGroupSetBoundary;
    private final CompetitionGroupSetDao competitionGroupSetDao;

    public CompetitionGroupSetGateway(
            CompetitionGroupSetBoundary competitionGroupSetBoundary,
            CompetitionGroupSetDao competitionGroupSetDao
    ) {
        this.competitionGroupSetBoundary = competitionGroupSetBoundary;
        this.competitionGroupSetDao = competitionGroupSetDao;
    }

    public void create(CompetitionGroupSet competitionGroupSet) {
        Preconditions.checkNotNull(competitionGroupSet);
        org.coner.hibernate.entity.CompetitionGroupSet hCompetitionGroupSet = competitionGroupSetBoundary
                .toHibernateEntity(competitionGroupSet);
        competitionGroupSetDao.createOrUpdate(hCompetitionGroupSet);
        competitionGroupSetBoundary.merge(hCompetitionGroupSet, competitionGroupSet);
    }
}
