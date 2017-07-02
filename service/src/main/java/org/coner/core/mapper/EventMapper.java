package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.service.CompetitionGroupSetService;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.EventDao;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class EventMapper {

    private EventDao dao;
    private HandicapGroupSetMapper handicapGroupSetMapper;
    private HandicapGroupSetService handicapGroupSetService;
    private CompetitionGroupSetMapper competitionGroupSetMapper;
    private CompetitionGroupSetService competitionGroupSetService;

    // primary mappings

    @Mappings({
            @Mapping(source = "apiAddRequest.handicapGroupSetId", target = "handicapGroupSet"),
            @Mapping(source = "apiAddRequest.competitionGroupSetId", target = "competitionGroupSet")
    })
    public abstract EventAddPayload toDomainAddPayload(AddEventRequest apiAddRequest);

    @Mappings({
            @Mapping(source = "domainEntity.handicapGroupSet.id", target = "handicapGroupSetId"),
            @Mapping(source = "domainEntity.competitionGroupSet.id", target = "competitionGroupSetId")
    })
    public abstract EventApiEntity toApiEntity(Event domainEntity);

    public abstract List<EventApiEntity> toApiEntityList(List<Event> domainEntityList);

    public abstract EventHibernateEntity toHibernateEntity(EventAddPayload domainAddPayload);

    public EventHibernateEntity toHibernateEntity(Event domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public abstract void updateHibernateEntity(
            Event domainEntity,
            @MappingTarget EventHibernateEntity hibernateEntity
    );

    public abstract Event toDomainEntity(EventHibernateEntity hibernateEntity);

    public abstract List<Event> toDomainEntityList(List<EventHibernateEntity> hibernateEntityList);

    // secondary mappings

    public HandicapGroupSet toDomainHandicapGroupSet(String handicapGroupSetId) throws EntityNotFoundException {
        return handicapGroupSetService.getById(handicapGroupSetId);
    }

    public CompetitionGroupSet toDomainCompetitionGroupSet(String competitionGroupSetId)
            throws EntityNotFoundException {
        return competitionGroupSetService.getById(competitionGroupSetId);
    }

    public HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSet domainEntity) {
        return handicapGroupSetMapper.toHibernateEntity(domainEntity);
    }

    public CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSet domainEntity) {
        return competitionGroupSetMapper.toHibernateEntity(domainEntity);
    }

    public HandicapGroupSet toDomainEntity(HandicapGroupSetHibernateEntity hibernateEntity) {
        return handicapGroupSetMapper.toDomainEntity(hibernateEntity);
    }

    public CompetitionGroupSet toDomainEntity(CompetitionGroupSetHibernateEntity hibernateEntity) {
        return competitionGroupSetMapper.toDomainEntity(hibernateEntity);
    }


    // setters

    public void setDao(EventDao dao) {
        this.dao = dao;
    }

    public void setHandicapGroupSetMapper(HandicapGroupSetMapper handicapGroupSetMapper) {
        this.handicapGroupSetMapper = handicapGroupSetMapper;
    }

    public void setHandicapGroupSetService(HandicapGroupSetService handicapGroupSetService) {
        this.handicapGroupSetService = handicapGroupSetService;
    }

    public void setCompetitionGroupSetMapper(CompetitionGroupSetMapper competitionGroupSetMapper) {
        this.competitionGroupSetMapper = competitionGroupSetMapper;
    }

    public void setCompetitionGroupSetService(CompetitionGroupSetService competitionGroupSetService) {
        this.competitionGroupSetService = competitionGroupSetService;
    }
}
