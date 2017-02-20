package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.mapper.CompetitionGroupSetMapper;

public class CompetitionGroupSetGateway extends MapStructAbstractGateway<
        CompetitionGroupSetAddPayload,
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetDao> {

    @Inject
    public CompetitionGroupSetGateway(
            CompetitionGroupSetMapper competitionGroupSetMapper,
            CompetitionGroupSetDao dao
    ) {
        super(
                competitionGroupSetMapper::toHibernateEntity,
                competitionGroupSetMapper::toDomainEntity,
                competitionGroupSetMapper::toDomainEntitiesList,
                dao
        );
    }
}
