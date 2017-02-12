package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.mapper.EventMapper;
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
}
