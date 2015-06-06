package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.dao.HandicapGroupSetDao;

/**
 * HandicapGroupSetGateway wraps persistence layer interactions for HandicapGroupSet domain entities.
 */
public class HandicapGroupSetGateway {

    private final HandicapGroupSetBoundary handicapGroupSetBoundary;
    private final HandicapGroupSetDao handicapGroupSetDao;

    /**
     * Constructor for HandicapGroupSetGateway
     *
     * @param handicapGroupSetBoundary the HandicapGroupSetBoundary for converting Domain to/from Hibernate entities
     * @param handicapGroupSetDao      the HandicapGroupSetDao for interacting with the persistence layer
     */
    public HandicapGroupSetGateway(
            HandicapGroupSetBoundary handicapGroupSetBoundary,
            HandicapGroupSetDao handicapGroupSetDao
    ) {
        this.handicapGroupSetBoundary = handicapGroupSetBoundary;
        this.handicapGroupSetDao = handicapGroupSetDao;
    }

    /**
     * Create a new HandicapGroupSet entity
     *
     * @param handicapGroupSet the CompetitionGroupSet entity to create
     */
    public void create(HandicapGroupSet handicapGroupSet) {
        Preconditions.checkNotNull(handicapGroupSet);
        org.coner.hibernate.entity.HandicapGroupSet hHandicapGroupSet = handicapGroupSetBoundary
                .toHibernateEntity(handicapGroupSet);
        handicapGroupSetDao.createOrUpdate(hHandicapGroupSet);
        handicapGroupSetBoundary.merge(hHandicapGroupSet, handicapGroupSet);
    }
}
