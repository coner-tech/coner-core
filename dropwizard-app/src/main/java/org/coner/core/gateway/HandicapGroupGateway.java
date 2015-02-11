package org.coner.core.gateway;

import com.google.common.base.Preconditions;
import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.dao.HandicapGroupDao;

import java.util.List;

/**
 *
 */
public class HandicapGroupGateway {

    private final HandicapGroupBoundary handicapGroupBoundary;
    private final HandicapGroupDao handicapGroupDao;

    /**
     * Constructor for HandicapGroupGateway.
     *
     * @param handicapGroupBoundary the HandicapGroupBoundary for converting Domain entities to/from Hibernate entities
     * @param handicapGroupDao      the HandicapGroupDao for interacting with the persistence layer
     */
    public HandicapGroupGateway(HandicapGroupBoundary handicapGroupBoundary, HandicapGroupDao handicapGroupDao) {
        this.handicapGroupBoundary = handicapGroupBoundary;
        this.handicapGroupDao = handicapGroupDao;
    }

    /**
     * Persist a new HandicapGroup entity.
     *
     * @param handicapGroup the HandicapGroup entity to persist
     */
    public void create(HandicapGroup handicapGroup) {
        Preconditions.checkNotNull(handicapGroup);
        org.coner.hibernate.entity.HandicapGroup hibernateHandicapGroup =
                handicapGroupBoundary.toHibernateEntity(handicapGroup);
        handicapGroupDao.create(hibernateHandicapGroup);
        handicapGroupBoundary.merge(hibernateHandicapGroup, handicapGroup);
    }

    /**
     * Get all HandicapGroup entities.
     *
     * @return list of HandicapGroup entities
     */
    public List<HandicapGroup> getAll() {
        List<org.coner.hibernate.entity.HandicapGroup> handicapGroups = handicapGroupDao.findAll();
        return handicapGroupBoundary.toDomainEntities(handicapGroups);
    }
}
