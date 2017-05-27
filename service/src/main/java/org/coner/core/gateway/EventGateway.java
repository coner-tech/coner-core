package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.dao.EventDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.mapper.EventMapper;

public class EventGateway extends MapStructAbstractGateway<
        EventAddPayload,
        Event,
        EventHibernateEntity,
        EventDao> {

    @Inject
    public EventGateway(
            EventMapper eventMapper,
            EventDao dao
    ) {
        super(
                eventMapper::toHibernateEntity,
                eventMapper::updateHibernateEntity,
                eventMapper::toDomainEntity,
                eventMapper::toDomainEntityList,
                dao
        );
    }
}
