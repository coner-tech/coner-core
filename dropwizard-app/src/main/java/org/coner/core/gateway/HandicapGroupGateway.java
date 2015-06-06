package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

import com.google.common.base.*;
import java.util.List;

public class HandicapGroupGateway {

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final HandicapGroupDao handicapGroupDao;

    public HandicapGroupGateway(HandicapGroupBoundary handicapGroupBoundary, HandicapGroupDao handicapGroupDao) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.handicapGroupDao = handicapGroupDao;
    }

    public void create(HandicapGroup handicapGroup) {
        Preconditions.checkNotNull(handicapGroup);
        HandicapGroupHibernateEntity hibernateHandicapGroup =
                handicapGroupBoundary.toHibernateEntity(handicapGroup);
        handicapGroupDao.create(hibernateHandicapGroup);
        handicapGroupBoundary.merge(hibernateHandicapGroup, handicapGroup);
    }

    public List<HandicapGroup> getAll() {
        List<HandicapGroupHibernateEntity> handicapGroups = handicapGroupDao.findAll();
        return handicapGroupBoundary.toDomainEntities(handicapGroups);
    }

    public HandicapGroup findById(String handicapGroupId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(handicapGroupId));
        HandicapGroupHibernateEntity hibernateHandicapGroup = handicapGroupDao.findById(handicapGroupId);
        return handicapGroupBoundary.toDomainEntity(hibernateHandicapGroup);
    }
}
