package org.coner.core.gateway;

import org.coner.boundary.EventHibernateAddPayloadBoundary;
import org.coner.boundary.EventHibernateDomainBoundary;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.entity.EventHibernateEntity;

public class EventGateway extends AbstractGateway<
        Event,
        EventHibernateEntity,
        EventAddPayload,
        EventHibernateDomainBoundary,
        EventHibernateAddPayloadBoundary,
        EventDao> {

    public EventGateway(
            EventHibernateDomainBoundary entityBoundary,
            EventHibernateAddPayloadBoundary addPayloadBoundary,
            EventDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
