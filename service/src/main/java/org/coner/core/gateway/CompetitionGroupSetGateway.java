package org.coner.core.gateway;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.hibernate.entity.CompetitionGroupSetHibernateEntity;
import org.coner.core.mapper.CompetitionGroupSetMapper;

public class CompetitionGroupSetGateway extends MapStructAbstractGateway<
        CompetitionGroupSetAddPayload,
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetDao> {

    private final CompetitionGroupDao competitionGroupDao;

    @Inject
    public CompetitionGroupSetGateway(
            CompetitionGroupSetMapper mapper,
            CompetitionGroupSetDao dao,
            CompetitionGroupDao competitionGroupDao) {
        super(
                mapper::toHibernateEntity,
                mapper::updateHibernateEntity,
                mapper::toDomainEntity,
                mapper::toDomainEntityList,
                dao
        );
        this.competitionGroupDao = competitionGroupDao;
    }

    @Override
    public CompetitionGroupSet add(CompetitionGroupSetAddPayload payload) {
        CompetitionGroupSetHibernateEntity setEntity = domainAddPayloadToHibernateEntityConverter.convert(payload);
        setEntity.setCompetitionGroups(
                payload.getCompetitionGroupIds()
                        .stream()
                        .map(competitionGroupDao::findById)
                        .collect(Collectors.toSet())
        );
        dao.create(setEntity);
        return hibernateEntityToDomainEntityConverter.convert(setEntity);
    }
}
