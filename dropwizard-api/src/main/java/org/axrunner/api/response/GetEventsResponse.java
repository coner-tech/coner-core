package org.axrunner.api.response;


import org.axrunner.api.entity.Event;

import java.util.List;

/**
 *
 */
public class GetEventsResponse {

    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
