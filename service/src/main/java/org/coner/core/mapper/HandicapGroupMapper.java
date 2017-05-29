package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class HandicapGroupMapper {

    private HandicapGroupDao dao;

    public abstract HandicapGroupAddPayload toDomainAddPayload(AddHandicapGroupRequest apiAddRequest);

    public abstract HandicapGroupApiEntity toApiEntity(HandicapGroup domainEntity);

    public abstract List<HandicapGroupApiEntity> toApiEntityList(List<HandicapGroup> domainEntityList);

    public abstract HandicapGroupHibernateEntity toHibernateEntity(HandicapGroupAddPayload domainAddPayload);

    public HandicapGroupHibernateEntity toHibernateEntity(HandicapGroup domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public abstract void updateHibernateEntity(
            HandicapGroup domainEntity,
            @MappingTarget HandicapGroupHibernateEntity hibernateEntity
    );

    public abstract HandicapGroup toDomainEntity(HandicapGroupHibernateEntity hibernateEntity);

    public abstract List<HandicapGroup> toDomainEntityList(List<HandicapGroupHibernateEntity> hibernateEntityList);

    public void setDao(HandicapGroupDao dao) {
        this.dao = dao;
    }
}
