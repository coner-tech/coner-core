package org.coner.core.mapper;

import java.util.List;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.mapstruct.Mapper;

@Mapper(
        uses = {HandicapGroupMapper.class},
        config = ConerBaseMapStructConfig.class
)

public interface HandicapGroupSetMapper {

    HandicapGroupSetAddPayload toDomainAddPayload(AddHandicapGroupSetRequest apiAddRequest);

    HandicapGroupSetApiEntity toApiEntity(HandicapGroupSet domainEntity);

    List<HandicapGroupSetApiEntity> toApiEntityList(List<HandicapGroupSet> domainEntityList);

    HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSetAddPayload domainAddPayload);

    HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSet domainEntity);

    HandicapGroupSet toDomainEntity(HandicapGroupSetHibernateEntity hibernateEntity);

    List<HandicapGroupSet> toDomainEntityList(List<HandicapGroupSetHibernateEntity> hibernateEntityList);

}
