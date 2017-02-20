package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public interface CompetitionGroupMapper {

    CompetitionGroupAddPayload toAddPayload(AddCompetitionGroupRequest addCompetitionGroupRequest);

    CompetitionGroupApiEntity toApiEntity(CompetitionGroup competitionGroup);

    List<CompetitionGroupApiEntity> toApiEntitiesList(List<CompetitionGroup> competitionGroups);

    CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroupAddPayload competitionGroupAddPayload);

    CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroup competitionGroup);

    CompetitionGroup toDomainEntity(CompetitionGroupHibernateEntity competitionGroupHibernateEntity);

    List<CompetitionGroup> toDomainEntities(List<CompetitionGroupHibernateEntity> competitionGroupHibernateEntities);
}
