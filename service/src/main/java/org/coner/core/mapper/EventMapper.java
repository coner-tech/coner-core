package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.dao.EventDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class EventMapper {

    private EventDao dao;
    private HandicapGroupSetMapper handicapGroupSetMapper;

    public abstract EventAddPayload toDomainAddPayload(AddEventRequest apiAddRequest);

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

    public void setDao(EventDao dao) {
        this.dao = dao;
    }

    public void setHandicapGroupSetMapper(HandicapGroupSetMapper handicapGroupSetMapper) {
        this.handicapGroupSetMapper = handicapGroupSetMapper;
    }
}
