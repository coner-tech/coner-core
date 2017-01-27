package org.coner.dagger;

import javax.inject.Singleton;

import org.coner.exception.WebApplicationExceptionMapper;
import org.coner.resource.CompetitionGroupResource;
import org.coner.resource.CompetitionGroupSetResource;
import org.coner.resource.CompetitionGroupSetsResource;
import org.coner.resource.CompetitionGroupsResource;
import org.coner.resource.EventRegistrationResource;
import org.coner.resource.EventRegistrationsResource;
import org.coner.resource.EventResource;
import org.coner.resource.EventsResource;
import org.coner.resource.HandicapGroupResource;
import org.coner.resource.HandicapGroupSetsResource;
import org.coner.resource.HandicapGroupsResource;

import dagger.Component;

@Singleton
@Component(modules = { ConerModule.class })
public interface JerseyRegistrationComponent {
    // Resources
    EventResource eventResource();
    EventsResource eventsResource();
    EventRegistrationResource eventRegistrationResource();
    EventRegistrationsResource eventRegistrationsResource();
    HandicapGroupResource handicapGroupResource();
    HandicapGroupsResource handicapGroupsResource();
    HandicapGroupSetsResource handicapGroupSetsResource();
    CompetitionGroupResource competitionGroupResource();
    CompetitionGroupsResource competitionGroupsResource();
    CompetitionGroupSetResource competitionGroupSetResource();
    CompetitionGroupSetsResource competitionGroupSetsResource();

    // Exception Mappers
    WebApplicationExceptionMapper webApplicationExceptionMapper();
}
