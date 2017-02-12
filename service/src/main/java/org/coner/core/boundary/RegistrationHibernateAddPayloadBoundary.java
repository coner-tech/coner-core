package org.coner.core.boundary;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.mapper.RegistrationMapper;

public class RegistrationHibernateAddPayloadBoundary implements Boundary<
        RegistrationHibernateEntity,
        RegistrationAddPayload> {

    private final RegistrationMapper mapper;

    @Inject
    public RegistrationHibernateAddPayloadBoundary(RegistrationMapper registrationMapper) {
        this.mapper = registrationMapper;
    }

    @Override
    public RegistrationAddPayload toRemoteEntity(RegistrationHibernateEntity localEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegistrationHibernateEntity toLocalEntity(RegistrationAddPayload remoteEntity) {
        return mapper.toHibernateEntity(remoteEntity);
    }

    @Override
    public List<RegistrationAddPayload> toRemoteEntities(List<RegistrationHibernateEntity> localEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RegistrationHibernateEntity> toLocalEntities(List<RegistrationAddPayload> remoteEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeLocalIntoRemote(
            RegistrationHibernateEntity fromLocalEntity,
            RegistrationAddPayload intoRemoteEntity
    ) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeRemoteIntoLocal(
            RegistrationAddPayload fromRemoteEntity,
            RegistrationHibernateEntity intoLocalEntity
    ) {
        throw new UnsupportedOperationException();
    }
}
