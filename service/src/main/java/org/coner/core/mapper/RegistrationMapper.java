package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.domain.service.CompetitionGroupEntityService;
import org.coner.core.domain.service.HandicapGroupEntityService;
import org.coner.core.domain.service.exception.EntityNotFoundException;
import org.coner.core.hibernate.dao.RegistrationDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.EventHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.RegistrationHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class RegistrationMapper {

    private RegistrationDao dao;
    private EventMapper eventMapper;
    private HandicapGroupMapper handicapGroupMapper;
    private HandicapGroupEntityService handicapGroupEntityService;
    private CompetitionGroupMapper competitionGroupMapper;
    private CompetitionGroupEntityService competitionGroupEntityService;

    // primary mappings

    @Mappings({
            @Mapping(source = "apiAddRequest.handicapGroupId", target = "handicapGroup"),
            @Mapping(source = "apiAddRequest.competitionGroupId", target = "competitionGroup")
    })
    public abstract RegistrationAddPayload toDomainAddPayload(AddRegistrationRequest apiAddRequest, String eventId);

    @Mappings({
            @Mapping(source = "domainEntity.handicapGroup.id", target = "handicapGroupId"),
            @Mapping(source = "domainEntity.competitionGroup.id", target = "competitionGroupId")
    })
    public abstract RegistrationApiEntity toApiEntity(Registration domainEntity);

    public abstract List<RegistrationApiEntity> toApiEntityList(List<Registration> domainEntityList);

    public abstract RegistrationHibernateEntity toHibernateEntity(RegistrationAddPayload domainAddPayload);

    public RegistrationHibernateEntity toHibernateEntity(Registration domainEntity) {
        if (domainEntity == null) {
            return null;
        }
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

    // secondary mappings

    public HandicapGroup toHandicapGroupDomainEntity(String handicapGroupId) throws EntityNotFoundException {
        return handicapGroupEntityService.getById(handicapGroupId);
    }

    public CompetitionGroup toCompetitionGroupDomainEntity(String competitionGroupId) throws EntityNotFoundException {
        return competitionGroupEntityService.getById(competitionGroupId);
    }

    public HandicapGroupHibernateEntity toHibernateEntity(HandicapGroup domainEntity) {
        return handicapGroupMapper.toHibernateEntity(domainEntity);
    }

    public CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroup domainEntity) {
        return competitionGroupMapper.toHibernateEntity(domainEntity);
    }

    public HandicapGroup toDomainEntity(HandicapGroupHibernateEntity hibernateEntity) {
        return handicapGroupMapper.toDomainEntity(hibernateEntity);
    }

    public CompetitionGroup toDomainEntity(CompetitionGroupHibernateEntity hibernateEntity) {
        return competitionGroupMapper.toDomainEntity(hibernateEntity);
    }

    // setters

    public void setDao(RegistrationDao dao) {
        this.dao = dao;
    }

    public void setEventMapper(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public void setHandicapGroupMapper(HandicapGroupMapper handicapGroupMapper) {
        this.handicapGroupMapper = handicapGroupMapper;
    }

    public void setHandicapGroupEntityService(HandicapGroupEntityService handicapGroupEntityService) {
        this.handicapGroupEntityService = handicapGroupEntityService;
    }

    public void setCompetitionGroupMapper(CompetitionGroupMapper competitionGroupMapper) {
        this.competitionGroupMapper = competitionGroupMapper;
    }

    public void setCompetitionGroupEntityService(CompetitionGroupEntityService competitionGroupEntityService) {
        this.competitionGroupEntityService = competitionGroupEntityService;
    }
}
