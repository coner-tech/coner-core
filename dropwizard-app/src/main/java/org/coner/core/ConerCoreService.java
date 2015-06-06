package org.coner.core;

import org.coner.core.domain.*;
import org.coner.core.exception.EventRegistrationMismatchException;
import org.coner.core.gateway.*;

import com.google.common.base.*;
import java.util.List;

public class ConerCoreService {

    private final EventGateway eventGateway;
    private final RegistrationGateway registrationGateway;
    private final CompetitionGroupGateway competitionGroupGateway;
    private final CompetitionGroupSetGateway competitionGroupSetGateway;
    private final HandicapGroupGateway handicapGroupGateway;
    private final HandicapGroupSetGateway handicapGroupSetGateway;

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
        this.competitionGroupGateway = competitionGroupGateway;
        this.handicapGroupGateway = handicapGroupGateway;
        this.handicapGroupSetGateway = handicapGroupSetGateway;
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

    public void addRegistration(Event event, Registration registration) {
        Preconditions.checkNotNull(registration);
        Preconditions.checkNotNull(event);
        registration.setEvent(event);
        registrationGateway.create(registration);
    }

    public List<Registration> getRegistrations(Event event) {
        Preconditions.checkNotNull(event);
        return registrationGateway.getAllWith(event);
    }

    public void addCompetitionGroup(CompetitionGroup competitionGroup) {
        Preconditions.checkNotNull(competitionGroup);
        competitionGroupGateway.create(competitionGroup);
    }

    public List<CompetitionGroup> getCompetitionGroups() {
        return competitionGroupGateway.getAll();
    }

    public CompetitionGroup getCompetitionGroup(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return competitionGroupGateway.findById(id);
    }

    public void addCompetitionGroupSet(CompetitionGroupSet competitionGroupSet) {
        Preconditions.checkNotNull(competitionGroupSet);
        competitionGroupSetGateway.create(competitionGroupSet);
    }

    public void addHandicapGroup(HandicapGroup handicapGroup) {
        Preconditions.checkNotNull(handicapGroup);
        handicapGroupGateway.create(handicapGroup);
    }

    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroupGateway.getAll();
    }

    public HandicapGroup getHandicapGroup(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id));
        return handicapGroupGateway.findById(id);
    }

    public void addHandicapGroupSet(HandicapGroupSet handicapGroupSet) {
        Preconditions.checkNotNull(handicapGroupSet);
        handicapGroupSetGateway.create(handicapGroupSet);
    }
}
