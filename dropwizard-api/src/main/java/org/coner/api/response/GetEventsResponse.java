package org.coner.api.response;


import org.coner.api.entity.Event;

import java.util.List;

public class GetEventsResponse {

    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
