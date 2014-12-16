package com.cebesius.axrunner.core.model.gateway;

import com.cebesius.axrunner.core.model.domain.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class EventGateway {

    public List<Event> getAll() {
        // TODO: implement for real
        int count = (int) Math.round(Math.random() * 10);
        List<Event> allEvents = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            allEvents.add(buildFakeEvent(i + 1));
        }
        return allEvents;
    }

    private Event buildFakeEvent(int number) {
        return new Event(
                UUID.randomUUID().toString(),
                String.format("Event #%d", number)
        );
    }


    public Event findById(String eventId) {
        return new Event(eventId, String.format("Event #%d", Math.round(Math.random() * 10)));
    }

    public Event create(String name) {
        return new Event(
                UUID.randomUUID().toString(),
                name
        );
    }
}
