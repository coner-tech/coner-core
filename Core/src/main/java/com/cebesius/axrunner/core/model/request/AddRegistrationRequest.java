package com.cebesius.axrunner.core.model.request;

/**
 *
 */
public class AddRegistrationRequest {

    private String eventId;
    private String firstName;
    private String lastName;

    public AddRegistrationRequest(String eventId, String firstName, String lastName) {
        this.eventId = eventId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
}
