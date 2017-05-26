package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public interface HandicapGroupMapper {

    HandicapGroupAddPayload toDomainAddPayload(AddHandicapGroupRequest apiAddRequest);

    HandicapGroupApiEntity toApiEntity(HandicapGroup domainEntity);

    List<HandicapGroupApiEntity> toApiEntityList(List<HandicapGroup> domainEntityList);

    HandicapGroupHibernateEntity toHibernateEntity(HandicapGroupAddPayload domainAddPayload);

    HandicapGroupHibernateEntity toHibernateEntity(HandicapGroup domainEntity);

    void updateHibernateEntity(
            HandicapGroup domainEntity,
            @MappingTarget HandicapGroupHibernateEntity hibernateEntity
    );

    HandicapGroup toDomainEntity(HandicapGroupHibernateEntity hibernateEntity);

    List<HandicapGroup> toDomainEntityList(List<HandicapGroupHibernateEntity> hibernateEntityList);
}
