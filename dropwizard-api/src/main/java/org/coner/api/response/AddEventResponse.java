package org.coner.api.response;

import org.coner.api.entity.Event;

/**
 * API response object when an Event was added
 */
public class AddEventResponse {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
