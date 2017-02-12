package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.resource.CompetitionGroupResource;
import org.coner.core.resource.CompetitionGroupSetResource;
import org.coner.core.resource.CompetitionGroupSetsResource;
import org.coner.core.resource.CompetitionGroupsResource;
import org.coner.core.resource.DomainServiceExceptionMapper;
import org.coner.core.resource.EventRegistrationResource;
import org.coner.core.resource.EventRegistrationsResource;
import org.coner.core.resource.EventResource;
import org.coner.core.resource.EventsResource;
import org.coner.core.resource.HandicapGroupResource;
import org.coner.core.resource.HandicapGroupSetsResource;
import org.coner.core.resource.HandicapGroupsResource;

import dagger.Component;

@Singleton
@Component(modules = { ConerModule.class, MapStructModule.class })
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
    DomainServiceExceptionMapper domainServiceExceptionMapper();
}
