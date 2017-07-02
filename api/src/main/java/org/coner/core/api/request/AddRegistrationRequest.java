package org.coner.core.api.request;

import java.time.Year;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddRegistrationRequest {

    @NotNull
    private AddPerson person;
    @NotNull
    private AddCar car;
    @NotBlank
    private String handicapGroupId;
    @NotBlank
    private String competitionGroupId;
    @NotBlank
    private String number;
    @NotNull
    private boolean checkedIn;

    public AddPerson getPerson() {
        return person;
    }

    public void setPerson(AddPerson person) {
        this.person = person;
    }

    public AddCar getCar() {
        return car;
    }

    public void setCar(AddCar car) {
        this.car = car;
    }

    public String getHandicapGroupId() {
        return handicapGroupId;
    }

    public void setHandicapGroupId(String handicapGroupId) {
        this.handicapGroupId = handicapGroupId;
    }

    public String getCompetitionGroupId() {
        return competitionGroupId;
    }

    public void setCompetitionGroupId(String competitionGroupId) {
        this.competitionGroupId = competitionGroupId;
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

    public static class AddPerson {

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

    public static class AddCar {
        @NotBlank
        private Year year;
        @NotBlank
        private String make;
        @NotBlank
        private String model;
        private String trim;
        @NotBlank
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
