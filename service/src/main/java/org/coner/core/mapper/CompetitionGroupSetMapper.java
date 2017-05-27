package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        uses = {CompetitionGroupMapper.class},
        config = ConerBaseMapStructConfig.class
)

public interface CompetitionGroupSetMapper {

    CompetitionGroupSetAddPayload toDomainAddPayload(AddCompetitionGroupSetRequest apiAddRequest);

    CompetitionGroupSetApiEntity toApiEntity(CompetitionGroupSet domainEntity);

    List<CompetitionGroupSetApiEntity> toApiEntityList(List<CompetitionGroupSet> domainEntityList);

    CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSetAddPayload domainAddPayload);

    CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSet domainEntity);

    void updateHibernateEntity(
            CompetitionGroupSet domainEntity,
            @MappingTarget CompetitionGroupSetHibernateEntity hibernateEntity
    );

    CompetitionGroupSet toDomainEntity(CompetitionGroupSetHibernateEntity hibernateEntity);

    List<CompetitionGroupSet> toDomainEntityList(
            List<CompetitionGroupSetHibernateEntity> hibernateEntityList
    );

}
