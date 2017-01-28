package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.exception.WebApplicationExceptionMapper;
import org.coner.core.resource.CompetitionGroupResource;
import org.coner.core.resource.CompetitionGroupSetResource;
import org.coner.core.resource.CompetitionGroupSetsResource;
import org.coner.core.resource.CompetitionGroupsResource;
import org.coner.core.resource.EventRegistrationResource;
import org.coner.core.resource.EventRegistrationsResource;
import org.coner.core.resource.EventResource;
import org.coner.core.resource.EventsResource;
import org.coner.core.resource.HandicapGroupResource;
import org.coner.core.resource.HandicapGroupSetsResource;
import org.coner.core.resource.HandicapGroupsResource;
import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;

@Module
public class MockitoJerseyRegistrationModule {

    @Provides
    @Singleton
    public EventResource getEventResource() {
        return Mockito.mock(EventResource.class);
    }

    @Provides
    @Singleton
    public EventsResource getEventsResource() {
        return Mockito.mock(EventsResource.class);
    }

    @Provides
    @Singleton
    public EventRegistrationResource getEventRegistrationResource() {
        return Mockito.mock(EventRegistrationResource.class);
    }

    @Provides
    @Singleton
    public EventRegistrationsResource getEventRegistrationsResource() {
        return Mockito.mock(EventRegistrationsResource.class);
    }

    @Provides
    @Singleton
    public HandicapGroupResource getHandicapGroupResource() {
        return Mockito.mock(HandicapGroupResource.class);
    }

    @Provides
    @Singleton
    public HandicapGroupsResource getHandicapGroupsResource() {
        return Mockito.mock(HandicapGroupsResource.class);
    }

    @Provides
    @Singleton
    public HandicapGroupSetsResource getHandicapGroupSetsResource() {
        return Mockito.mock(HandicapGroupSetsResource.class);
    }

    @Provides
    @Singleton
    public CompetitionGroupResource getCompetitionGroupResource() {
        return Mockito.mock(CompetitionGroupResource.class);
    }

    @Provides
    @Singleton
    public CompetitionGroupsResource getCompetitionGroupsResource() {
        return Mockito.mock(CompetitionGroupsResource.class);
    }

    @Provides
    @Singleton
    public CompetitionGroupSetResource getCompetitionGroupSetResource() {
        return Mockito.mock(CompetitionGroupSetResource.class);
    }

    @Provides
    @Singleton
    public CompetitionGroupSetsResource getCompetitionGroupSetsResource() {
        return Mockito.mock(CompetitionGroupSetsResource.class);
    }

    @Provides
    @Singleton
    public WebApplicationExceptionMapper getWebApplicationExceptionMapper() {
        return Mockito.mock(WebApplicationExceptionMapper.class);
    }
}
