package org.coner.core;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.service.*;
import org.coner.core.exception.EventMismatchException;
import org.coner.core.gateway.*;

import com.google.common.base.*;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConerCoreService {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final CompetitionGroupGateway competitionGroupGateway;
    private final CompetitionGroupSetGateway competitionGroupSetGateway;
    private final HandicapGroupGateway handicapGroupGateway;
    private final HandicapGroupSetGateway handicapGroupSetGateway;

    public ConerCoreService(
            EventService eventService,
            RegistrationService registrationService,
            CompetitionGroupGateway competitionGroupGateway,
            CompetitionGroupSetGateway competitionGroupSetGateway,
            HandicapGroupGateway handicapGroupGateway,
            HandicapGroupSetGateway handicapGroupSetGateway
    ) {
        this.eventService = eventService;
        this.registrationService = registrationService;
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

    public Registration getRegistration(String eventId, String registrationId) throws EventMismatchException {
        Event event = eventService.getById(checkNotNull(eventId));
        return registrationService.getByIdWithEvent(checkNotNull(registrationId), event);
    }

    public void addRegistration(Event event, Registration registration) {
        registration.setEvent(checkNotNull(event));
        registrationService.add(registration);
    }

    public List<Registration> getRegistrations(Event event) {
        checkNotNull(event);
        return registrationService.getAllWith(event);
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
