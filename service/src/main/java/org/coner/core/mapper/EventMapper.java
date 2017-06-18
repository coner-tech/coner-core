package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.domain.service.HandicapGroupSetService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.EventDao;
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

    @Mappings({
            @Mapping(source = "apiAddRequest.handicapGroupSetId", target = "handicapGroupSet")
    })
    public abstract EventAddPayload toDomainAddPayload(AddEventRequest apiAddRequest);

    public HandicapGroupSet toDomainHandicapGroupSet(String handicapGroupSetId) throws EntityNotFoundException {
        return handicapGroupSetService.getById(handicapGroupSetId);
    }

    public abstract EventApiEntity toApiEntity(Event domainEntity);

    public abstract List<EventApiEntity> toApiEntityList(List<Event> domainEntityList);

    public abstract EventHibernateEntity toHibernateEntity(EventAddPayload domainAddPayload);

    public EventHibernateEntity toHibernateEntity(Event domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSet domainEntity) {
        return handicapGroupSetMapper.toHibernateEntity(domainEntity);
    }

    public abstract void updateHibernateEntity(
            Event domainEntity,
            @MappingTarget EventHibernateEntity hibernateEntity
    );

    public abstract Event toDomainEntity(EventHibernateEntity hibernateEntity);

    public HandicapGroupSet toDomainEntity(HandicapGroupSetHibernateEntity hibernateEntity) {
        return handicapGroupSetMapper.toDomainEntity(hibernateEntity);
    }

    public abstract List<Event> toDomainEntityList(List<EventHibernateEntity> hibernateEntityList);

    public void setDao(EventDao dao) {
        this.dao = dao;
    }

    public void setHandicapGroupSetMapper(HandicapGroupSetMapper handicapGroupSetMapper) {
        this.handicapGroupSetMapper = handicapGroupSetMapper;
    }

    public void setHandicapGroupSetService(HandicapGroupSetService handicapGroupSetService) {
        this.handicapGroupSetService = handicapGroupSetService;
    }
}
