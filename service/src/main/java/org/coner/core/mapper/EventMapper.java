package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.EventApiEntity;
import org.coner.core.api.request.AddEventRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public interface EventMapper {

    EventAddPayload toDomainAddPayload(AddEventRequest apiAddRequest);

    EventApiEntity toApiEntity(Event domainEntity);

    List<EventApiEntity> toApiEntityList(List<Event> domainEntityList);

    EventHibernateEntity toHibernateEntity(EventAddPayload domainAddPayload);

    EventHibernateEntity toHibernateEntity(Event domainEntity);

    Event toDomainEntity(EventHibernateEntity hibernateEntity);

    List<Event> toDomainEntityList(List<EventHibernateEntity> hibernateEntityList);
}
