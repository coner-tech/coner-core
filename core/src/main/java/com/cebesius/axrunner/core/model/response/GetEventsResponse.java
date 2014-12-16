package com.cebesius.axrunner.core.model.response;

import com.cebesius.axrunner.core.model.domain.Event;

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
