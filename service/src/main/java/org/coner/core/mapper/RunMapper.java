package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.RunApiEntity;
import org.coner.core.api.request.AddRunRequest;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.entity.Run;
import org.coner.core.domain.payload.RunAddPayload;
import org.coner.core.hibernate.dao.RunDao;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.coner.core.hibernate.entity.RunHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class RunMapper {

    private RunDao dao;
    private EventMapper eventMapper;
    private RegistrationMapper registrationMapper;

    public abstract RunAddPayload toDomainAddPayload(AddRunRequest apiAddRequest, String eventId);

    public abstract RunApiEntity toApiEntity(Run domainEntity);

    public abstract List<RunApiEntity> toApiEntityList(List<Run> domainEntityList);

    public abstract RunHibernateEntity toHibernateEntity(RunAddPayload domainAddPayload);

    public RunHibernateEntity toHibernateEntity(Run domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public EventHibernateEntity toHibernateEntity(Event domainEntity) {
        return eventMapper.toHibernateEntity(domainEntity);
    }

    public RegistrationHibernateEntity toHibernateEntity(Registration domainEntity) {
        return registrationMapper.toHibernateEntity(domainEntity);
    }

    public abstract void updateHibernateEntity(
            Run domainEntity,
            @MappingTarget RunHibernateEntity hibernateEntity
    );

    public abstract Run toDomainEntity(RunHibernateEntity hibernateEntity);

    public Event toDomainEntity(EventHibernateEntity hibernateEntity) {
        return eventMapper.toDomainEntity(hibernateEntity);
    }

    public Registration toDomainEntity(RegistrationHibernateEntity hibernateEntity) {
        return registrationMapper.toDomainEntity(hibernateEntity);
    }

    public abstract List<Run> toDomainEntityList(List<RunHibernateEntity> hibernateEntityList);

    public void setDao(RunDao dao) {
        this.dao = dao;
    }

    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public void setRegistrationMapper(RegistrationMapper registrationMapper) {
        this.registrationMapper = registrationMapper;
    }
}
