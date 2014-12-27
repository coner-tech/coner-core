package org.axrunner.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.axrunner.core.domain.Event;
import org.axrunner.core.domain.Registration;
import org.axrunner.hibernate.gateway.EventGateway;
import org.axrunner.hibernate.gateway.RegistrationGateway;

import java.util.List;

/**
 *
 */
public class AxRunnerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;

    public AxRunnerCoreService(EventGateway eventGateway, RegistrationGateway registrationGateway) {
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
