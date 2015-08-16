package org.coner.core.domain.service;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.gateway.EventGateway;

public class EventService extends AbstractDomainService<Event, EventAddPayload, EventGateway> {
    public EventService(EventGateway gateway) {
        super(Event.class, gateway);
    }
}
