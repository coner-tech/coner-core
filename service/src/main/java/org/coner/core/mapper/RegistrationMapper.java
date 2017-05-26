package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class,
        uses = EventMapper.class
)
public interface RegistrationMapper {

    RegistrationAddPayload toDomainAddPayload(AddRegistrationRequest apiAddRequest, String eventId);

    RegistrationApiEntity toApiEntity(Registration domainEntity);

    List<RegistrationApiEntity> toApiEntityList(List<Registration> domainEntityList);

    RegistrationHibernateEntity toHibernateEntity(RegistrationAddPayload domainAddPayload);

    RegistrationHibernateEntity toHibernateEntity(Registration domainEntity);

    void updateHibernateEntity(
            Registration domainEntity,
            @MappingTarget RegistrationHibernateEntity hibernateEntity
    );

    Registration toDomainEntity(RegistrationHibernateEntity hibernateEntity);

    List<Registration> toDomainEntityList(List<RegistrationHibernateEntity> hibernateEntityList);
}
