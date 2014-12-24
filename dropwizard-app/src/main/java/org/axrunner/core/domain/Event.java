package org.axrunner.core.domain;

import java.util.Date;

/**
 *
 */
public class Event extends DomainEntity {

    private String eventId;
    private String name;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public boolean hasDate() {
        return date != null;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
