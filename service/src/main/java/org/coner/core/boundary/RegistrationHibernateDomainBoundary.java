package org.coner.core.boundary;

import java.util.List;

import javax.inject.Inject;

import org.coner.core.domain.entity.Registration;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.mapper.RegistrationMapper;

public class RegistrationHibernateDomainBoundary implements Boundary<RegistrationHibernateEntity, Registration> {

    private final RegistrationMapper mapper;

    @Inject
    public RegistrationHibernateDomainBoundary(RegistrationMapper registrationMapper) {
        this.mapper = registrationMapper;
    }

    @Override
    public Registration toRemoteEntity(RegistrationHibernateEntity localEntity) {
        return mapper.toDomainEntity(localEntity);
    }

    @Override
    public RegistrationHibernateEntity toLocalEntity(Registration remoteEntity) {
        return mapper.toHibernateEntity(remoteEntity);
    }

    @Override
    public List<Registration> toRemoteEntities(List<RegistrationHibernateEntity> localEntities) {
        return mapper.toDomainEntities(localEntities);
    }

    @Override
    public List<RegistrationHibernateEntity> toLocalEntities(List<Registration> remoteEntities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeLocalIntoRemote(RegistrationHibernateEntity fromLocalEntity, Registration intoRemoteEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void mergeRemoteIntoLocal(Registration fromRemoteEntity, RegistrationHibernateEntity intoLocalEntity) {
        throw new UnsupportedOperationException();
    }
}
