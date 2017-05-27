package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public interface CompetitionGroupMapper {

    CompetitionGroupAddPayload toDomainAddPayload(AddCompetitionGroupRequest apiAddRequest);

    CompetitionGroupApiEntity toApiEntity(CompetitionGroup domainEntity);

    List<CompetitionGroupApiEntity> toApiEntityList(List<CompetitionGroup> domainEntityList);

    CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroupAddPayload domainAddPayload);

    CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroup domainEntity);

    void updateHibernateEntity(
            CompetitionGroup domainEntity,
            @MappingTarget CompetitionGroupHibernateEntity hibernateEntity
    );

    CompetitionGroup toDomainEntity(CompetitionGroupHibernateEntity hibernateEntity);

    List<CompetitionGroup> toDomainEntityList(List<CompetitionGroupHibernateEntity> hibernateEntityList);
}
