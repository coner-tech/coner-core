package org.coner.core.boundary;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.payload.EventAddPayload;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.mapper.EventMapper;

public class EventHibernateAddPayloadBoundary implements Boundary<EventHibernateEntity, EventAddPayload> {

    private final EventMapper mapper;

    @Inject
    public EventHibernateAddPayloadBoundary(EventMapper eventMapper) {
        this.mapper = eventMapper;
    }

    @Override
    public EventAddPayload toRemoteEntity(EventHibernateEntity localEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EventHibernateEntity toLocalEntity(EventAddPayload remoteEntity) {
        return mapper.toHibernateEntity(remoteEntity);
    }

    @Override
    public List<EventAddPayload> toRemoteEntities(List<EventHibernateEntity> localEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<EventHibernateEntity> toLocalEntities(List<EventAddPayload> remoteEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeLocalIntoRemote(EventHibernateEntity fromLocalEntity, EventAddPayload intoRemoteEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeRemoteIntoLocal(EventAddPayload fromRemoteEntity, EventHibernateEntity intoLocalEntity) {
        throw new UnsupportedOperationException();
    }
}
