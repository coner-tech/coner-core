package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.mapper.CompetitionGroupMapper;

public class CompetitionGroupGateway extends MapStructAbstractGateway<
        CompetitionGroupAddPayload,
        CompetitionGroup,
        CompetitionGroupHibernateEntity,
        CompetitionGroupDao> {

    @Inject
    public CompetitionGroupGateway(
            CompetitionGroupMapper competitionGroupMapper,
            CompetitionGroupDao dao
    ) {
        super(
                competitionGroupMapper::toHibernateEntity,
                competitionGroupMapper::toDomainEntity,
                competitionGroupMapper::toDomainEntityList,
                dao
        );
    }
}
