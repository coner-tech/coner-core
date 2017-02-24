package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.mapper.HandicapGroupSetMapper;

public class HandicapGroupSetGateway extends MapStructAbstractGateway<
        HandicapGroupSetAddPayload,
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetDao> {

    @Inject
    public HandicapGroupSetGateway(
            HandicapGroupSetMapper mapper,
            HandicapGroupSetDao dao
    ) {
        super(
                mapper::toHibernateEntity,
                mapper::toDomainEntity,
                mapper::toDomainEntityList,
                dao
        );
    }
}
