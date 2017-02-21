package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.mapper.CompetitionGroupSetMapper;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.HandicapGroupMapper;
import org.coner.core.mapper.HandicapGroupSetMapper;
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

    @Provides
    @Singleton
    public CompetitionGroupSetMapper competitionGroupSetMapper() {
        return Mappers.getMapper(CompetitionGroupSetMapper.class);
    }

    @Provides
    @Singleton
    public HandicapGroupMapper handicapGroupMapper() {
        return Mappers.getMapper(HandicapGroupMapper.class);
    }

    @Provides
    @Singleton
    public HandicapGroupSetMapper handicapGroupSetMapper() {
        return Mappers.getMapper(HandicapGroupSetMapper.class);
    }
}
