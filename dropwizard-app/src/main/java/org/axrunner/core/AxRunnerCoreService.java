package org.axrunner.core;

import org.axrunner.core.domain.Event;
import org.axrunner.hibernate.gateway.EventGateway;

import java.util.List;

/**
 *
 */
public class AxRunnerCoreService {

    private final EventGateway eventGateway;

    public AxRunnerCoreService(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    public List<Event> getEvents() {
        return eventGateway.getAll();
    }

    public void addEvent(Event event) {
        eventGateway.create(event);
    }

    public Event getEvent(String id) {
        return eventGateway.findById(id);
    }
}
