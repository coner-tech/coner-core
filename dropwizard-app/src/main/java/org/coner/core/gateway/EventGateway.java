package org.coner.core.gateway;

import org.coner.boundary.EventHibernateDomainBoundary;
import org.coner.core.domain.Event;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.entity.EventHibernateEntity;

public class EventGateway extends AbstractGateway<
        Event,
        EventHibernateEntity,
        EventHibernateDomainBoundary,
        EventDao> {

    public EventGateway(EventHibernateDomainBoundary boundary, EventDao dao) {
        super(boundary, dao);
    }
}
