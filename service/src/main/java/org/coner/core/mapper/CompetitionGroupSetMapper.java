package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.mapstruct.Mapper;

@Mapper(
        uses = {CompetitionGroupMapper.class},
        config = ConerBaseMapStructConfig.class
)

public interface CompetitionGroupSetMapper {

    CompetitionGroupSetAddPayload toAddPayload(AddCompetitionGroupSetRequest s);

    CompetitionGroupSetApiEntity toApiEntity(CompetitionGroupSet s);

    List<CompetitionGroupSetApiEntity> toApiEntitiesList(List<CompetitionGroupSet> s);

    CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSetAddPayload s);

    CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSet s);

    CompetitionGroupSet toDomainEntity(CompetitionGroupSetHibernateEntity s);

    List<CompetitionGroupSet> toDomainEntitiesList(
            List<CompetitionGroupSetHibernateEntity> s
    );

}
