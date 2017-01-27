package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.boundary.EventHibernateAddPayloadBoundary;
import org.coner.core.boundary.EventHibernateDomainBoundary;
import org.coner.core.api.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.dao.EventDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;

public class EventGateway extends AbstractGateway<
        Event,
        EventHibernateEntity,
        EventAddPayload,
        EventHibernateDomainBoundary,
        EventHibernateAddPayloadBoundary,
        EventDao> {

    @Inject
    public EventGateway(
            EventHibernateDomainBoundary entityBoundary,
            EventHibernateAddPayloadBoundary addPayloadBoundary,
            EventDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
