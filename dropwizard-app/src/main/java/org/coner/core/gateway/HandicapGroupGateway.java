package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.boundary.HandicapGroupHibernateAddPayloadBoundary;
import org.coner.boundary.HandicapGroupHibernateDomainBoundary;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

public class HandicapGroupGateway extends AbstractGateway<
        HandicapGroup,
        HandicapGroupHibernateEntity,
        HandicapGroupAddPayload,
        HandicapGroupHibernateDomainBoundary,
        HandicapGroupHibernateAddPayloadBoundary,
        HandicapGroupDao> {

    @Inject
    public HandicapGroupGateway(
            HandicapGroupHibernateDomainBoundary entityBoundary,
            HandicapGroupHibernateAddPayloadBoundary addPayloadBoundary,
            HandicapGroupDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
