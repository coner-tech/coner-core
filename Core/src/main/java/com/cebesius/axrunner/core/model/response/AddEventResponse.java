package com.cebesius.axrunner.core.model.response;

import com.cebesius.axrunner.core.model.domain.Event;

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
