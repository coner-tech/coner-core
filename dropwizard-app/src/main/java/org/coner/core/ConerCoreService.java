package org.coner.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

/**
 *
 */
public class ConerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;

    public ConerCoreService(EventGateway eventGateway, RegistrationGateway registrationGateway) {
        this.eventGateway = eventGateway;
        this.registrationGateway = registrationGateway;
    }

    public List<Event> getEvents() {
        return eventGateway.getAll();
    }

    public void addEvent(Event event) {
        Preconditions.checkNotNull(event);
        eventGateway.create(event);
    }

    public Event getEvent(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return eventGateway.findById(id);
    }

    public List<Registration> getRegistrations(Event event) {
        Preconditions.checkNotNull(event);
        return registrationGateway.getAllWith(event);
    }
}
