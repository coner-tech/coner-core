package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

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
        CompetitionGroupHibernateEntity competitionGroupHibernateEntity = competitionGroupBoundary.toHibernateEntity(
                competitionGroup
        );
        competitionGroupDao.createOrUpdate(competitionGroupHibernateEntity);
        competitionGroupBoundary.merge(competitionGroupHibernateEntity, competitionGroup);
    }

    public List<CompetitionGroup> getAll() {
        List<CompetitionGroupHibernateEntity> competitionGroupHibernateEntities = competitionGroupDao.findAll();
        return competitionGroupBoundary.toDomainEntities(competitionGroupHibernateEntities);
    }

    public CompetitionGroup findById(String competitionGroupId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(competitionGroupId));
        CompetitionGroupHibernateEntity competitionGroupHibernateEntity = competitionGroupDao.findById(
                competitionGroupId
        );
        return competitionGroupBoundary.toDomainEntity(competitionGroupHibernateEntity);
    }
}
