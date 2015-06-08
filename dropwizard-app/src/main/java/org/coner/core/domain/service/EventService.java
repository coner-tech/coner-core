package org.coner.core.domain.service;

import org.coner.core.domain.entity.Event;
import org.coner.core.gateway.EventGateway;

public class EventService extends AbstractDomainService<Event, EventGateway> {
    public EventService(EventGateway gateway) {
        super(gateway);
    }
}
