package org.coner.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.core.domain.CompetitionGroup;
import org.coner.core.domain.Event;
import org.coner.core.domain.HandicapGroup;
import org.coner.core.domain.Registration;
import org.coner.core.gateway.CompetitionGroupGateway;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.HandicapGroupGateway;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

/**
 * The ConerCoreService is the domain level implementation of all functionality in the Coner core project.
 */
public class ConerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;
    private final HandicapGroupGateway handicapGroupGateway;
    private final CompetitionGroupGateway competitionGroupGateway;

    /**
     * Constructor for ConerCoreService.
     *
     * @param eventGateway            the EventGateway
     * @param registrationGateway     the RegistrationGateway
     * @param handicapGroupGateway    the HandicapGroupGateway
     * @param competitionGroupGateway the CompetitionGroupGateway
     */
    public ConerCoreService(EventGateway eventGateway,
                            RegistrationGateway registrationGateway,
                            HandicapGroupGateway handicapGroupGateway,
                            CompetitionGroupGateway competitionGroupGateway) {
        this.eventGateway = eventGateway;
        this.registrationGateway = registrationGateway;
        this.handicapGroupGateway = handicapGroupGateway;
        this.competitionGroupGateway = competitionGroupGateway;
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
     * Get an Event by id.
     *
     * @param id the id of the event
     * @return the event with id or null if not found
     */
    public Event getEvent(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return eventGateway.findById(id);
    }

    /**
     * Get a Registration by id.
     *
     * @param id the id of the Registration
     * @return the Registration with id or null if not found
     */
    public Registration getRegistration(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return registrationGateway.findById(id);
    }

    /**
     * Add a registration entry.
     *
     * @param event        the Event the Registration is being added for
     * @param registration the Registration to add
     */
    public void addRegistration(Event event, Registration registration) {
        Preconditions.checkNotNull(registration);
        Preconditions.checkNotNull(event);
        registration.setEvent(event);
        registrationGateway.create(registration);
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

    /**
     * Add a HandicapGroup.
     *
     * @param handicapGroup the HandicapGroup entity to add
     */
    public void addHandicapGroup(HandicapGroup handicapGroup) {
        Preconditions.checkNotNull(handicapGroup);
        handicapGroupGateway.create(handicapGroup);
    }

    /**
     * Add a competition group.
     *
     * @param competitionGroup the Event entity to add
     */
    public void addCompetitionGroup(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        competitionGroupGateway.create(competitionGroup);
    }

    /**
     * Get all HandicapGroups.
     *
     * @return a List of HandicapGroup.
     */
    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroupGateway.getAll();
    }
}
