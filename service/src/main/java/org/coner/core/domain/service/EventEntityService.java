package org.coner.core.domain.service;

import javax.inject.Inject;

import org.coner.core.api.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.gateway.EventGateway;

public class EventEntityService extends AbstractEntityService<Event, EventAddPayload, EventGateway> {

    @Inject
    public EventEntityService(EventGateway gateway) {
        super(Event.class, gateway);
    }
}
