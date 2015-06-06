package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.dao.HandicapGroupSetDao;

import com.google.common.base.Preconditions;

public class HandicapGroupSetGateway {

    private final HandicapGroupSetBoundary handicapGroupSetBoundary;
    private final HandicapGroupSetDao handicapGroupSetDao;

    public HandicapGroupSetGateway(
            HandicapGroupSetBoundary handicapGroupSetBoundary,
            HandicapGroupSetDao handicapGroupSetDao
    ) {
        this.handicapGroupSetBoundary = handicapGroupSetBoundary;
        this.handicapGroupSetDao = handicapGroupSetDao;
    }

    public void create(HandicapGroupSet handicapGroupSet) {
        Preconditions.checkNotNull(handicapGroupSet);
        org.coner.hibernate.entity.HandicapGroupSet hHandicapGroupSet = handicapGroupSetBoundary
                .toHibernateEntity(handicapGroupSet);
        handicapGroupSetDao.createOrUpdate(hHandicapGroupSet);
        handicapGroupSetBoundary.merge(hHandicapGroupSet, handicapGroupSet);
    }
}
