package org.coner.core.domain.payload;

import java.util.Objects;

import org.coner.core.domain.entity.Event;

public class RegistrationAddPayload extends DomainAddPayload {
    private String eventId;
    private Event event;
    private String firstName;
    private String lastName;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationAddPayload that = (RegistrationAddPayload) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(event, that.event) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, event, firstName, lastName);
    }
}
