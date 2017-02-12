package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.RegistrationMapper;
import org.mapstruct.factory.Mappers;

import dagger.Module;
import dagger.Provides;

@Module
public class MapStructModule {

    @Provides
    @Singleton
    public EventMapper eventMapper() {
        return Mappers.getMapper(EventMapper.class);
    }

    @Provides
    @Singleton
    public RegistrationMapper registrationMapper() {
        return Mappers.getMapper(RegistrationMapper.class);
    }

    @Provides
    @Singleton
    public CompetitionGroupMapper competitionGroupMapper() {
        return Mappers.getMapper(CompetitionGroupMapper.class);
    }
}
