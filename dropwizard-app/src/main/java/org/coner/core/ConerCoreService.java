package org.coner.core;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.service.EventService;
import org.coner.core.exception.EventRegistrationMismatchException;
import org.coner.core.gateway.*;

import com.google.common.base.*;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConerCoreService {

    private final EventService eventService;
    private final RegistrationGateway registrationGateway;
    private final CompetitionGroupGateway competitionGroupGateway;
    private final CompetitionGroupSetGateway competitionGroupSetGateway;
    private final HandicapGroupGateway handicapGroupGateway;
    private final HandicapGroupSetGateway handicapGroupSetGateway;

    public ConerCoreService(
            EventService eventService,
            RegistrationGateway registrationGateway,
            CompetitionGroupGateway competitionGroupGateway,
            CompetitionGroupSetGateway competitionGroupSetGateway,
            HandicapGroupGateway handicapGroupGateway,
            HandicapGroupSetGateway handicapGroupSetGateway
    ) {
        this.eventService = eventService;
        this.registrationGateway = registrationGateway;
        this.competitionGroupSetGateway = competitionGroupSetGateway;
        this.competitionGroupGateway = competitionGroupGateway;
        this.handicapGroupGateway = handicapGroupGateway;
        this.handicapGroupSetGateway = handicapGroupSetGateway;
    }

    public List<Event> getEvents() {
        return eventService.getAll();
    }

    public void addEvent(Event event) {
        eventService.add(checkNotNull(event));
    }

    public Event getEvent(String id) {
        return eventService.getById(checkNotNull(id));
    }

    public Registration getRegistration(String eventId, String registrationId)
            throws EventRegistrationMismatchException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(eventId));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(registrationId));
        Event event = eventService.getById(eventId);
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
        checkNotNull(registration);
        checkNotNull(event);
        registration.setEvent(event);
        registrationGateway.create(registration);
    }

    public List<Registration> getRegistrations(Event event) {
        checkNotNull(event);
        return registrationGateway.getAllWith(event);
    }

    public void addCompetitionGroup(CompetitionGroup competitionGroup) {
        checkNotNull(competitionGroup);
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
        checkNotNull(competitionGroupSet);
        competitionGroupSetGateway.create(competitionGroupSet);
    }

    public void addHandicapGroup(HandicapGroup handicapGroup) {
        checkNotNull(handicapGroup);
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
        checkNotNull(handicapGroupSet);
        handicapGroupSetGateway.create(handicapGroupSet);
    }
}
