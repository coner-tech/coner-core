package org.coner.core.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.coner.core.api.entity.CompetitionGroupApiEntity;
import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.google.common.collect.Sets;

@Mapper(
        config = ConerBaseMapStructConfig.class
)
public abstract class CompetitionGroupSetMapper {

    private CompetitionGroupSetDao dao;
    private CompetitionGroupMapper competitionGroupMapper;

    public abstract CompetitionGroupSetAddPayload toDomainAddPayload(AddCompetitionGroupSetRequest apiAddRequest);

    public abstract CompetitionGroupSetApiEntity toApiEntity(CompetitionGroupSet domainEntity);

    public Set<CompetitionGroupApiEntity> toInnerApiEntitySet(Set<CompetitionGroup> innerDomainEntitySet) {
        return Optional.ofNullable(innerDomainEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(competitionGroupMapper::toApiEntity)
                .collect(Collectors.toSet());
    }

    public abstract List<CompetitionGroupSetApiEntity> toApiEntityList(List<CompetitionGroupSet> domainEntityList);

    public abstract CompetitionGroupSetHibernateEntity toHibernateEntity(
            CompetitionGroupSetAddPayload domainAddPayload
    );

    public CompetitionGroupSetHibernateEntity toHibernateEntity(CompetitionGroupSet domainEntity) {
        return dao.findById(domainEntity.getId());
    }

    public Set<CompetitionGroupHibernateEntity> toInnerHibernateEntitySet(Set<CompetitionGroup> innerDomainEntitySet) {
        return Optional.ofNullable(innerDomainEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(competitionGroupMapper::toHibernateEntity)
                .collect(Collectors.toSet());
    }

    public abstract void updateHibernateEntity(
            CompetitionGroupSet domainEntity,
            @MappingTarget CompetitionGroupSetHibernateEntity hibernateEntity
    );

    public abstract CompetitionGroupSet toDomainEntity(CompetitionGroupSetHibernateEntity hibernateEntity);

    public Set<CompetitionGroup> toInnerDomainEntitySet(Set<CompetitionGroupHibernateEntity> innerHibernateEntitySet) {
        return Optional.ofNullable(innerHibernateEntitySet).orElse(Sets.newHashSet())
                .stream()
                .map(competitionGroupMapper::toDomainEntity)
                .collect(Collectors.toSet());
    }

    public abstract List<CompetitionGroupSet> toDomainEntityList(
            List<CompetitionGroupSetHibernateEntity> hibernateEntityList
    );

    public void setDao(CompetitionGroupSetDao dao) {
        this.dao = dao;
    }

    public void setCompetitionGroupMapper(CompetitionGroupMapper competitionGroupMapper) {
        this.competitionGroupMapper = competitionGroupMapper;
    }
}
