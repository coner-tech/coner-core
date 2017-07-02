package org.coner.core.api.request;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddRegistrationRequest {

    @NotNull
    private AddPerson person;
    @NotBlank
    private String handicapGroupId;
    @NotBlank
    private String competitionGroupId;
    @NotBlank
    private String number;

    public AddPerson getPerson() {
        return person;
    }

    public void setPerson(AddPerson person) {
        this.person = person;
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
}
