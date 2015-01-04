package org.coner.util;

import org.coner.api.entity.Event;
import org.coner.api.entity.Registration;

import java.util.Date;

/**
 *
 */
public final class ApiEntityUtils {

    private ApiEntityUtils() {
    }

    // Event
    public static Event fullApiEvent() {
        return fullApiEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullApiEvent(String eventId, String eventName, Date eventDate) {
        Event apiEvent = new Event();
        apiEvent.setId(eventId);
        apiEvent.setName(eventName);
        apiEvent.setDate(eventDate);
        return apiEvent;
    }

    public static org.coner.api.entity.Registration.Event partialApiEvent() {
        org.coner.api.entity.Registration.Event event = new org.coner.api.entity.Registration.Event();
        event.setId(TestConstants.EVENT_ID);
        return event;
    }

    // Registration
    public static Registration fullApiRegistration() {
        return fullApiRegistration(TestConstants.REGISTRATION_ID, TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME);
    }

    public static Registration fullApiRegistration(String registrationId, String registrationFirstName,
                                                   String registrationLastName) {
        Registration apiRegistration = new Registration();
        apiRegistration.setId(registrationId);
        apiRegistration.setFirstName(registrationFirstName);
        apiRegistration.setLastName(registrationLastName);
        apiRegistration.setEvent(partialApiEvent());
        return apiRegistration;
    }

}
