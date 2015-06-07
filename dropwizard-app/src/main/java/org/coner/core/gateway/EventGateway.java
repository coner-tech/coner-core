package org.coner.core.gateway;

import org.coner.boundary.EventBoundary;
import org.coner.core.domain.Event;
import org.coner.hibernate.dao.EventDao;
import org.coner.hibernate.entity.EventHibernateEntity;

public class EventGateway extends AbstractGateway<
        Event,
        EventHibernateEntity,
        EventBoundary,
        EventDao> {

    public EventGateway(EventBoundary boundary, EventDao dao) {
        super(boundary, dao);
    }
}
