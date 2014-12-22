package org.axrunner.api.response;

import org.axrunner.api.entity.Event;

/**
 *
 */
public class AddEventResponse {

    private final Event event;

    public AddEventResponse(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
