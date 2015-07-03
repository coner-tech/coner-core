package org.coner.core;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.service.*;
import org.coner.core.exception.EventMismatchException;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ConerCoreService {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final CompetitionGroupService competitionGroupService;
    private final CompetitionGroupSetService competitionGroupSetService;
    private final HandicapGroupService handicapGroupService;
    private final HandicapGroupSetService handicapGroupSetService;

    public ConerCoreService(
            EventService eventService,
            RegistrationService registrationService,
            CompetitionGroupService competitionGroupService,
            CompetitionGroupSetService competitionGroupSetService,
            HandicapGroupService handicapGroupService,
            HandicapGroupSetService handicapGroupSetService
    ) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.competitionGroupSetService = competitionGroupSetService;
        this.competitionGroupService = competitionGroupService;
        this.handicapGroupService = handicapGroupService;
        this.handicapGroupSetService = handicapGroupSetService;
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
        competitionGroupService.add(checkNotNull(competitionGroup));
    }

    public List<CompetitionGroup> getCompetitionGroups() {
        return competitionGroupService.getAll();
    }

    public CompetitionGroup getCompetitionGroup(String id) {
        return competitionGroupService.getById(checkNotNull(id));
    }

    public void addCompetitionGroupSet(CompetitionGroupSet competitionGroupSet) {
        competitionGroupSetService.add(checkNotNull(competitionGroupSet));
    }

    public void addHandicapGroup(HandicapGroup handicapGroup) {
        handicapGroupService.add(checkNotNull(handicapGroup));
    }

    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroupService.getAll();
    }

    public HandicapGroup getHandicapGroup(String id) {
        return handicapGroupService.getById(checkNotNull(id));
    }

    public void addHandicapGroupSet(HandicapGroupSet handicapGroupSet) {
        handicapGroupSetService.add(checkNotNull(handicapGroupSet));
    }
}
