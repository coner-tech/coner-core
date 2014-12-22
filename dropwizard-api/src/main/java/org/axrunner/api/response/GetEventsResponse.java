package org.axrunner.api.response;


import org.axrunner.api.entity.Event;

import java.util.List;

/**
 *
 */
public class GetEventsResponse {

    private final List<Event> events;

    public GetEventsResponse(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
