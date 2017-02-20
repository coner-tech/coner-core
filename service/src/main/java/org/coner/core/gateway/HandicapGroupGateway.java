package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.mapper.HandicapGroupMapper;

public class HandicapGroupGateway extends MapStructAbstractGateway<
        HandicapGroupAddPayload,
        HandicapGroup,
        HandicapGroupHibernateEntity,
        HandicapGroupDao> {

    @Inject
    public HandicapGroupGateway(
            HandicapGroupMapper mapper,
            HandicapGroupDao dao
    ) {
        super(
                mapper::toHibernateEntity,
                mapper::toDomainEntity,
                mapper::toDomainEntityList,
                dao
        );
    }
}
