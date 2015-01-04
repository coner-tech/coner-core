package org.coner.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

/**
 * The ConerCoreService is the domain level implementation of all functionality in the Coner core project.
 */
public class ConerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;

    /**
     * Constructor for ConerCoreService.
     *
     * @param eventGateway        the EventGateway
     * @param registrationGateway the RegistrationGateway
     */
    public ConerCoreService(EventGateway eventGateway, RegistrationGateway registrationGateway) {
        this.eventGateway = eventGateway;
        this.registrationGateway = registrationGateway;
    }

    /**
     * Get all events.
     *
     * @return a list of all events
     */
    public List<Event> getEvents() {
        return eventGateway.getAll();
    }

    /**
     * Add an event.
     *
     * @param event the Event entity to add
     */
    public void addEvent(Event event) {
        Preconditions.checkNotNull(event);
        eventGateway.create(event);
    }

    /**
     * Get an event by id.
     *
     * @param id the id of the event
     * @return the event with id or null if not found
     */
    public Event getEvent(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return eventGateway.findById(id);
    }

    /**
     * Get all registrations for an event.
     *
     * @param event the event to get registrations
     * @return a list of all registrations for the event
     */
    public List<Registration> getRegistrations(Event event) {
        Preconditions.checkNotNull(event);
        return registrationGateway.getAllWith(event);
    }
}
