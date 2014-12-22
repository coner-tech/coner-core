package org.axrunner.core.domain;

/**
 *
 */
public class Registration {

    private String id;
    private String eventId;
    private String firstName;
    private String lastName;

    public Registration(String id, String eventId, String firstName, String lastName) {
        this.id = id;
        this.eventId = eventId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getEventId() {
        return eventId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
