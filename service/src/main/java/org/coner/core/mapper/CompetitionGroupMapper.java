package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.request.AddCompetitionGroupRequest;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class CompetitionGroupMapper {

    private CompetitionGroupDao dao;

    public abstract CompetitionGroupAddPayload toDomainAddPayload(AddCompetitionGroupRequest apiAddRequest);

    public abstract CompetitionGroupApiEntity toApiEntity(CompetitionGroup domainEntity);

    public abstract List<CompetitionGroupApiEntity> toApiEntityList(List<CompetitionGroup> domainEntityList);

    public abstract CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroupAddPayload domainAddPayload);

    public CompetitionGroupHibernateEntity toHibernateEntity(CompetitionGroup domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public abstract void updateHibernateEntity(
            CompetitionGroup domainEntity,
            @MappingTarget CompetitionGroupHibernateEntity hibernateEntity
    );

    public abstract CompetitionGroup toDomainEntity(CompetitionGroupHibernateEntity hibernateEntity);

    public abstract List<CompetitionGroup> toDomainEntityList(
            List<CompetitionGroupHibernateEntity> hibernateEntityList
    );

    public void setDao(CompetitionGroupDao dao) {
        this.dao = dao;
    }
}
