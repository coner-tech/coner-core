package org.coner.core.gateway;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.coner.core.mapper.HandicapGroupSetMapper;

public class HandicapGroupSetGateway extends MapStructAbstractGateway<
        HandicapGroupSetAddPayload,
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetDao> {

    private final HandicapGroupDao handicapGroupDao;

    @Inject
    public HandicapGroupSetGateway(
            HandicapGroupSetMapper mapper,
            HandicapGroupSetDao dao,
            HandicapGroupDao handicapGroupDao) {
        super(
                mapper::toHibernateEntity,
                mapper::updateHibernateEntity,
                mapper::toDomainEntity,
                mapper::toDomainEntityList,
                dao
        );
        this.handicapGroupDao = handicapGroupDao;
    }

    @Override
    public HandicapGroupSet add(HandicapGroupSetAddPayload payload) {
        HandicapGroupSetHibernateEntity setEntity = domainAddPayloadToHibernateEntityConverter.map(payload);
        setEntity.setHandicapGroups(
                payload.getHandicapGroupIds()
                        .stream()
                        .map(handicapGroupDao::findById)
                        .collect(Collectors.toSet()));
        dao.create(setEntity);
        return hibernateEntityToDomainEntityConverter.map(setEntity);
    }
}
