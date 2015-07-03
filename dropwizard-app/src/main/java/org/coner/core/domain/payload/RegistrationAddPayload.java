package org.coner.core.domain.payload;

import org.coner.core.domain.entity.Event;

public class RegistrationAddPayload extends DomainAddPayload {
    public String eventId;
    public Event event;
    public String firstName;
    public String lastName;
}
