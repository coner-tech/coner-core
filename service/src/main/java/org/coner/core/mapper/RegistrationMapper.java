package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.hibernate.dao.RegistrationDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class RegistrationMapper {

    private RegistrationDao dao;
    private EventMapper eventMapper;

    public abstract RegistrationAddPayload toDomainAddPayload(AddRegistrationRequest apiAddRequest, String eventId);

    public abstract RegistrationApiEntity toApiEntity(Registration domainEntity);

    public abstract List<RegistrationApiEntity> toApiEntityList(List<Registration> domainEntityList);

    public abstract RegistrationHibernateEntity toHibernateEntity(RegistrationAddPayload domainAddPayload);

    public RegistrationHibernateEntity toHibernateEntity(Registration domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public EventHibernateEntity toHibernateEntity(Event event) {
        return eventMapper.toHibernateEntity(event);
    }

    public abstract void updateHibernateEntity(
            Registration domainEntity,
            @MappingTarget RegistrationHibernateEntity hibernateEntity
    );

    public abstract Registration toDomainEntity(RegistrationHibernateEntity hibernateEntity);

    public Event toDomainEntity(EventHibernateEntity eventHibernateEntity) {
        return eventMapper.toDomainEntity(eventHibernateEntity);
    }

    public abstract List<Registration> toDomainEntityList(List<RegistrationHibernateEntity> hibernateEntityList);

    public void setDao(RegistrationDao dao) {
        this.dao = dao;
    }

    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }
}
