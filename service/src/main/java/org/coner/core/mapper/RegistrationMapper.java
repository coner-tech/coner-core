package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.mapstruct.Mapper;

@Mapper(uses = EventMapper.class)
public interface RegistrationMapper {

    RegistrationAddPayload toAddPayload(AddRegistrationRequest addRegistrationRequest, String eventId);

    RegistrationApiEntity toApiEntity(Registration registration);

    List<RegistrationApiEntity> toApiEntitiesList(List<Registration> registrations);

    RegistrationHibernateEntity toHibernateEntity(RegistrationAddPayload registrationAddPayload);

    RegistrationHibernateEntity toHibernateEntity(Registration registration);

    Registration toDomainEntity(RegistrationHibernateEntity registrationHibernateEntity);

    List<Registration> toDomainEntities(List<RegistrationHibernateEntity> registrationHibernateEntities);
}
