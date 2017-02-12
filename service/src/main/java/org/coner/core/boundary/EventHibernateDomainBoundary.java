package org.coner.core.boundary;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Event;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.mapper.EventMapper;

public class EventHibernateDomainBoundary implements Boundary<EventHibernateEntity, Event> {

    private final EventMapper mapper;

    @Inject
    public EventHibernateDomainBoundary(EventMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Event toRemoteEntity(EventHibernateEntity localEntity) {
        return mapper.toDomainEntity(localEntity);
    }

    @Override
    public EventHibernateEntity toLocalEntity(Event remoteEntity) {
        return mapper.toHibernateEntity(remoteEntity);
    }

    @Override
    public List<Event> toRemoteEntities(List<EventHibernateEntity> localEntities) {
        return mapper.toDomainEntities(localEntities);
    }

    @Override
    public List<EventHibernateEntity> toLocalEntities(List<Event> remoteEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeLocalIntoRemote(EventHibernateEntity fromLocalEntity, Event intoRemoteEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeRemoteIntoLocal(Event fromRemoteEntity, EventHibernateEntity intoLocalEntity) {
        throw new UnsupportedOperationException();
    }
}
