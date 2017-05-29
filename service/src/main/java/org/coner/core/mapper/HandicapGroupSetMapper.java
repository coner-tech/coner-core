package org.coner.core.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.entity.HandicapGroupHibernateEntity;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.google.common.collect.Sets;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class HandicapGroupSetMapper {

    private HandicapGroupSetDao dao;
    private HandicapGroupMapper handicapGroupMapper;

    public abstract HandicapGroupSetAddPayload toDomainAddPayload(AddHandicapGroupSetRequest apiAddRequest);

    public abstract HandicapGroupSetApiEntity toApiEntity(HandicapGroupSet domainEntity);

    public Set<HandicapGroupApiEntity> toInnerApiEntitySet(Set<HandicapGroup> innerDomainEntitySet) {
        return Optional.ofNullable(innerDomainEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(handicapGroupMapper::toApiEntity)
                .collect(Collectors.toSet());
    }

    public abstract List<HandicapGroupSetApiEntity> toApiEntityList(List<HandicapGroupSet> domainEntityList);

    public abstract HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSetAddPayload domainAddPayload);

    public HandicapGroupSetHibernateEntity toHibernateEntity(HandicapGroupSet domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public Set<HandicapGroupHibernateEntity> toInnerHibernateEntitySet(Set<HandicapGroup> innerDomainEntitySet) {
        return Optional.ofNullable(innerDomainEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(handicapGroupMapper::toHibernateEntity)
                .collect(Collectors.toSet());
    }

    public abstract void updateHibernateEntity(
            HandicapGroupSet domainEntity,
            @MappingTarget HandicapGroupSetHibernateEntity hibernateEntity
    );

    public abstract HandicapGroupSet toDomainEntity(HandicapGroupSetHibernateEntity hibernateEntity);

    public Set<HandicapGroup> toInnerDomainEntitySet(Set<HandicapGroupHibernateEntity> innerHibernateEntitySet) {
        return Optional.ofNullable(innerHibernateEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(handicapGroupMapper::toDomainEntity)
                .collect(Collectors.toSet());
    }

    public abstract List<HandicapGroupSet> toDomainEntityList(
            List<HandicapGroupSetHibernateEntity> hibernateEntityList
    );

    public void setDao(HandicapGroupSetDao dao) {
        this.dao = dao;
    }

    public void setHandicapGroupMapper(HandicapGroupMapper handicapGroupMapper) {
        this.handicapGroupMapper = handicapGroupMapper;
    }
}
