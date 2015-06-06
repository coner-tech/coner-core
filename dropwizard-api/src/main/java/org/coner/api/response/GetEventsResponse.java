package org.coner.api.response;


import org.coner.api.entity.EventApiEntity;

import java.util.List;

public class GetEventsResponse {

    private List<EventApiEntity> events;

    public List<EventApiEntity> getEvents() {
        return events;
    }

    public void setEvents(List<EventApiEntity> events) {
        this.events = events;
    }
}
