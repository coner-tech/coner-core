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

    EventAddPayload toAddPayload(AddEventRequest addEventRequest);

    EventApiEntity toApiEntity(Event event);

    List<EventApiEntity> toApiEntitiesList(List<Event> events);

    EventHibernateEntity toHibernateEntity(EventAddPayload eventAddPayload);

    EventHibernateEntity toHibernateEntity(Event event);

    Event toDomainEntity(EventHibernateEntity eventHibernateEntity);

    List<Event> toDomainEntities(List<EventHibernateEntity> eventHibernateEntities);
}
