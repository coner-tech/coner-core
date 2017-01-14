package org.coner.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.CompetitionGroupService;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.EventService;
import org.coner.core.domain.service.HandicapGroupService;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.RegistrationService;
import org.coner.core.exception.EntityNotFoundException;
import org.coner.core.exception.EventMismatchException;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

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
