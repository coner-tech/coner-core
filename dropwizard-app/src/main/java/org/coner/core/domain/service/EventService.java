package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.gateway.EventGateway;

public class EventService extends AbstractDomainService<Event, EventAddPayload, EventGateway> {

    @Inject
    public EventService(EventGateway gateway) {
        super(Event.class, gateway);
    }
}
