package org.coner.api.response;


import java.util.List;

import org.coner.api.entity.EventApiEntity;

public class GetEventsResponse {

    private List<EventApiEntity> events;

    public List<EventApiEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventApiEntity> events) {
        this.events = events;
    }
}
