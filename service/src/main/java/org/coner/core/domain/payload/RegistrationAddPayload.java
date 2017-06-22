package org.coner.core.domain.payload;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;

public class RegistrationAddPayload extends DomainAddPayload {
    private String eventId;
    private Event event;
    private String firstName;
    private String lastName;
    private HandicapGroup handicapGroup;

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

    public HandicapGroup getHandicapGroup() {
        return handicapGroup;
    }

    public void setHandicapGroup(HandicapGroup handicapGroup) {
        this.handicapGroup = handicapGroup;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
