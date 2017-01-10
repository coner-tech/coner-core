package org.coner.core;

import org.coner.core.domain.entity.*;
import org.coner.core.domain.payload.*;
import org.coner.core.domain.service.*;
import org.coner.core.exception.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
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

    public Event addEvent(EventAddPayload addPayload) {
        return eventService.add(checkNotNull(addPayload));
    }

    public Event getEvent(String id) throws EntityNotFoundException {
        return eventService.getById(checkNotNull(id));
    }

    public Registration getRegistration(String eventId, String registrationId)
            throws EventMismatchException, EntityNotFoundException {
        Event event = eventService.getById(checkNotNull(eventId));
        return registrationService.getByIdWithEvent(checkNotNull(registrationId), event);
    }

    public Registration addRegistration(RegistrationAddPayload addPayload) throws EntityNotFoundException {
        checkNotNull(addPayload);
        addPayload.event = eventService.getById(checkNotNull(addPayload.eventId));
        return registrationService.add(addPayload);
    }

    public List<Registration> getRegistrations(String eventId) throws EntityNotFoundException {
        Event event = eventService.getById(checkNotNull(eventId));
        return registrationService.getAllWith(event);
    }

    public CompetitionGroup addCompetitionGroup(CompetitionGroupAddPayload addPayload) {
        return competitionGroupService.add(checkNotNull(addPayload));
    }

    public List<CompetitionGroup> getCompetitionGroups() {
        return competitionGroupService.getAll();
    }

    public CompetitionGroup getCompetitionGroup(String id) throws EntityNotFoundException {
        return competitionGroupService.getById(checkNotNull(id));
    }

    public CompetitionGroupSet addCompetitionGroupSet(CompetitionGroupSetAddPayload addPayload)
            throws EntityNotFoundException {
        Preconditions.checkNotNull(addPayload);
        Preconditions.checkNotNull(addPayload.competitionGroupIds);
        ImmutableSet.Builder<CompetitionGroup> competitionGroupsBuilder = ImmutableSet.builder();
        for (String competitionGroupId : addPayload.competitionGroupIds) {
            competitionGroupsBuilder.add(competitionGroupService.getById(competitionGroupId));
        }
        addPayload.competitionGroups = competitionGroupsBuilder.build();
        return competitionGroupSetService.add(checkNotNull(addPayload));
    }

    public HandicapGroup addHandicapGroup(HandicapGroupAddPayload addPayload) {
        return handicapGroupService.add(checkNotNull(addPayload));
    }

    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroupService.getAll();
    }

    public HandicapGroup getHandicapGroup(String id) throws EntityNotFoundException {
        return handicapGroupService.getById(checkNotNull(id));
    }

    public HandicapGroupSet addHandicapGroupSet(HandicapGroupSetAddPayload addPayload) {
        return handicapGroupSetService.add(checkNotNull(addPayload));
    }

    public List<CompetitionGroupSet> getCompetitionGroupSets() {
        return competitionGroupSetService.getAll();
    }

    public CompetitionGroupSet getCompetitionGroupSet(String id) throws EntityNotFoundException {
        return competitionGroupSetService.getById(checkNotNull(id));
    }
}
