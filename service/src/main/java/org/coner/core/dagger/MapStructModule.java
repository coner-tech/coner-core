package org.coner.core.dagger;

import javax.inject.Singleton;

import org.coner.core.domain.service.EventEntityService;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.RegistrationEntityService;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.hibernate.dao.EventDao;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.dao.RegistrationDao;
import org.coner.core.hibernate.dao.RunDao;
import org.coner.core.mapper.CompetitionGroupMapper;
import org.coner.core.mapper.CompetitionGroupSetMapper;
import org.coner.core.mapper.EventMapper;
import org.coner.core.mapper.HandicapGroupMapper;
import org.coner.core.mapper.HandicapGroupSetMapper;
import org.coner.core.mapper.RegistrationMapper;
import org.coner.core.mapper.RunMapper;
import org.mapstruct.factory.Mappers;

import dagger.Module;
import dagger.Provides;

@Module
public class MapStructModule {

    @Provides
    @Singleton
    public EventMapper eventMapper(
            EventDao dao,
            HandicapGroupSetMapper handicapGroupSetMapper,
            HandicapGroupSetService handicapGroupSetService
    ) {
        EventMapper mapper = Mappers.getMapper(EventMapper.class);
        mapper.setDao(dao);
        mapper.setHandicapGroupSetMapper(handicapGroupSetMapper);
        mapper.setHandicapGroupSetService(handicapGroupSetService);
        return mapper;
    }

    @Provides
    @Singleton
    public RegistrationMapper registrationMapper(RegistrationDao dao, EventMapper eventMapper) {
        RegistrationMapper mapper = Mappers.getMapper(RegistrationMapper.class);
        mapper.setDao(dao);
        mapper.setEventMapper(eventMapper);
        return mapper;
    }

    @Provides
    @Singleton
    public CompetitionGroupMapper competitionGroupMapper(CompetitionGroupDao dao) {
        CompetitionGroupMapper mapper = Mappers.getMapper(CompetitionGroupMapper.class);
        mapper.setDao(dao);
        return mapper;
    }

    @Provides
    @Singleton
    public CompetitionGroupSetMapper competitionGroupSetMapper(
            CompetitionGroupSetDao dao,
            CompetitionGroupMapper competitionGroupMapper
    ) {
        CompetitionGroupSetMapper mapper = Mappers.getMapper(CompetitionGroupSetMapper.class);
        mapper.setDao(dao);
        mapper.setCompetitionGroupMapper(competitionGroupMapper);
        return mapper;
    }

    @Provides
    @Singleton
    public HandicapGroupMapper handicapGroupMapper(HandicapGroupDao dao) {
        HandicapGroupMapper mapper = Mappers.getMapper(HandicapGroupMapper.class);
        mapper.setDao(dao);
        return mapper;
    }

    @Provides
    @Singleton
    public HandicapGroupSetMapper handicapGroupSetMapper(
            HandicapGroupSetDao dao,
            HandicapGroupMapper handicapGroupMapper
    ) {
        HandicapGroupSetMapper mapper = Mappers.getMapper(HandicapGroupSetMapper.class);
        mapper.setDao(dao);
        mapper.setHandicapGroupMapper(handicapGroupMapper);
        return mapper;
    }

    @Provides
    @Singleton
    public RunMapper runMapper(
            RunDao dao,
            EventMapper eventMapper,
            EventEntityService eventEntityService,
            RegistrationMapper registrationMapper,
            RegistrationEntityService registrationEntityService
    ) {
        RunMapper mapper = Mappers.getMapper(RunMapper.class);
        mapper.setDao(dao);
        mapper.setEventMapper(eventMapper);
        mapper.setEventEntityService(eventEntityService);
        mapper.setRegistrationMapper(registrationMapper);
        mapper.setRegistrationEntityService(registrationEntityService);
        return mapper;
    }
}
