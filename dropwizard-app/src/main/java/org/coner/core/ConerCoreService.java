package org.coner.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.coner.core.domain.CompetitionGroup;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.core.domain.Event;
import org.coner.core.domain.HandicapGroup;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.core.domain.Registration;
import org.coner.core.exception.EventRegistrationMismatchException;
import org.coner.core.gateway.CompetitionGroupGateway;
import org.coner.core.gateway.CompetitionGroupSetGateway;
import org.coner.core.gateway.EventGateway;
import org.coner.core.gateway.HandicapGroupGateway;
import org.coner.core.gateway.HandicapGroupSetGateway;
import org.coner.core.gateway.RegistrationGateway;

import java.util.List;

/**
 * The ConerCoreService is the domain level implementation of all functionality in the Coner core project.
 */
public class ConerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;
    private final CompetitionGroupGateway competitionGroupGateway;
    private final CompetitionGroupSetGateway competitionGroupSetGateway;
    private final HandicapGroupGateway handicapGroupGateway;
    private final HandicapGroupSetGateway handicapGroupSetGateway;

    /**
     * Constructor for ConerCoreService.
     *
     * @param eventGateway               the EventGateway
     * @param registrationGateway        the RegistrationGateway
     * @param competitionGroupGateway    the CompetitionGroupGateway
     * @param competitionGroupSetGateway the CompetitionGroupSetGateway
     * @param handicapGroupGateway       the HandicapGroupGateway
     * @param handicapGroupSetGateway    the HandicapGroupSetGateway
     */
    public ConerCoreService(EventGateway eventGateway,
                            RegistrationGateway registrationGateway,
                            CompetitionGroupGateway competitionGroupGateway,
                            CompetitionGroupSetGateway competitionGroupSetGateway,
                            HandicapGroupGateway handicapGroupGateway,
                            HandicapGroupSetGateway handicapGroupSetGateway
    ) {
        this.eventGateway = eventGateway;
        this.registrationGateway = registrationGateway;
        this.competitionGroupSetGateway = competitionGroupSetGateway;
        this.handicapGroupGateway = handicapGroupGateway;
        this.competitionGroupGateway = competitionGroupGateway;
        this.handicapGroupSetGateway = handicapGroupSetGateway;
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
     * @param eventId        the id of the Event the Registration belongs to
     * @param registrationId the id of the Registration
     * @return the Registration with id or null if not found
     * @throws org.coner.core.exception.EventRegistrationMismatchException if the Registration's Event id doesn't match
     *                                                                     eventId
     */
    public Registration getRegistration(String eventId, String registrationId)
            throws EventRegistrationMismatchException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(eventId));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(registrationId));
        Event event = eventGateway.findById(eventId);
        if (event == null) {
            return null;
        }
        Registration registration = registrationGateway.findById(registrationId);
        if (registration == null) {
            return null;
        }
        if (!registration.getEvent().getId().equals(eventId)) {
            throw new EventRegistrationMismatchException();
        }
        return registration;
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
     * Add a competition group.
     *
     * @param competitionGroup the Event entity to add
     */
    public void addCompetitionGroup(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        competitionGroupGateway.create(competitionGroup);
    }

    /**
     * Get all CompetitionGroups.
     *
     * @return a List of CompetitionGroup entities.
     */
    public List<CompetitionGroup> getCompetitionGroups() {
        return competitionGroupGateway.getAll();
    }

    /**
     * Get a CompetitionGroup by id.
     *
     * @param id the id of the CompetitionGroup
     * @return the CompetitionGroup with id or null if not found
     */
    public CompetitionGroup getCompetitionGroup(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return competitionGroupGateway.findById(id);
    }

    /**
     * Add a CompetitionGroupSet
     *
     * @param competitionGroupSet the CompetitionGroupSet entity to add
     */
    public void addCompetitionGroupSet(CompetitionGroupSet competitionGroupSet) {
        Preconditions.checkNotNull(competitionGroupSet);
        competitionGroupSetGateway.create(competitionGroupSet);
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
     * Get all HandicapGroups.
     *
     * @return a List of HandicapGroup.
     */
    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroupGateway.getAll();
    }

    /**
     * Get a HandicapGroup by id.
     *
     * @param id the id of the HandicapGroup
     * @return the HandicapGroup with id or null if not found
     */
    public HandicapGroup getHandicapGroup(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return handicapGroupGateway.findById(id);
    }

    /**
     * Add a HandicapGroupSet
     *
     * @param handicapGroupSet the HandicapGroupSet entity to add
     */
    public void addHandicapGroupSet(HandicapGroupSet handicapGroupSet) {
        Preconditions.checkNotNull(handicapGroupSet);
        handicapGroupSetGateway.create(handicapGroupSet);
    }
}
