package org.coner.core.domain.payload;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;

public class RegistrationAddPayload extends DomainAddPayload {
    private PersonAddPayload person;
    private String eventId;
    private Event event;
    private HandicapGroup handicapGroup;
    private CompetitionGroup competitionGroup;
    private String number;

    public PersonAddPayload getPerson() {
        return person;
    }

    public void setPerson(PersonAddPayload person) {
        this.person = person;
    }

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

    public HandicapGroup getHandicapGroup() {
        return handicapGroup;
    }

    public void setHandicapGroup(HandicapGroup handicapGroup) {
        this.handicapGroup = handicapGroup;
    }

    public CompetitionGroup getCompetitionGroup() {
        return competitionGroup;
    }

    public void setCompetitionGroup(CompetitionGroup competitionGroup) {
        this.competitionGroup = competitionGroup;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static class PersonAddPayload {

        private String firstName;
        private String middleName;
        private String lastName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }
    }
}
