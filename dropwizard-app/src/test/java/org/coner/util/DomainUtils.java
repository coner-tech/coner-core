package org.coner.util;

import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;

import java.util.Date;

/**
 *
 */
public final class DomainUtils {

    private DomainUtils() {
    }

    // Event
    public static Event fullDomainEvent() {
        return fullDomainEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static Event fullDomainEvent(String eventId, String eventName, Date eventDate) {
        Event domainEvent = new Event();
        domainEvent.setId(eventId);
        domainEvent.setName(eventName);
        domainEvent.setDate(eventDate);
        return domainEvent;
    }

    // Registration
    public static Registration fullDomainRegistration() {
        return fullDomainRegistration(TestConstants.REGISTRATION_ID, TestConstants.REGISTRATION_FIRSTNAME,
                TestConstants.REGISTRATION_LASTNAME);
    }

    public static Registration fullDomainRegistration(String registrationId, String registrationFirstName,
                                                      String registrationLastName) {
        Registration domainRegistration = new Registration();
        domainRegistration.setId(registrationId);
        domainRegistration.setFirstName(registrationFirstName);
        domainRegistration.setLastName(registrationLastName);
        domainRegistration.setEvent(DomainUtils.fullDomainEvent());
        return domainRegistration;
    }
}
