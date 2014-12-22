package org.axrunner.core.domain;

import org.joda.time.DateTime;

/**
 *
 */
public class Event extends DomainEntity {

    private String eventId;
    private String name;
    private DateTime date;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getDate() {
        return date;
    }

    public boolean hasDate() {
        return date != null;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
