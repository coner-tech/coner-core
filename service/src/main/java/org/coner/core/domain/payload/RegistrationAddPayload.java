package org.coner.core.domain.payload;

import java.time.Year;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;

public class RegistrationAddPayload extends DomainAddPayload {
    private PersonAddPayload person;
    private CarAddPayload car;
    private String eventId;
    private Event event;
    private HandicapGroup handicapGroup;
    private CompetitionGroup competitionGroup;
    private String number;
    private boolean checkedIn;

    public PersonAddPayload getPerson() {
        return person;
    }

    public void setPerson(PersonAddPayload person) {
        this.person = person;
    }

    public CarAddPayload getCar() {
        return car;
    }

    public void setCar(CarAddPayload car) {
        this.car = car;
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

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
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

    public static class CarAddPayload {

        private Year year;
        private String make;
        private String model;
        private String trim;
        private String color;

        public Year getYear() {
            return year;
        }

        public void setYear(Year year) {
            this.year = year;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getTrim() {
            return trim;
        }

        public void setTrim(String trim) {
            this.trim = trim;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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
