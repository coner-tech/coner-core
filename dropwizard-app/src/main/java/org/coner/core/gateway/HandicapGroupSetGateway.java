package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.boundary.HandicapGroupSetHibernateAddPayloadBoundary;
import org.coner.boundary.HandicapGroupSetHibernateDomainBoundary;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.hibernate.dao.HandicapGroupSetDao;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

public class HandicapGroupSetGateway extends AbstractGateway<
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetAddPayload,
        HandicapGroupSetHibernateDomainBoundary,
        HandicapGroupSetHibernateAddPayloadBoundary,
        HandicapGroupSetDao> {

    @Inject
    public HandicapGroupSetGateway(
            HandicapGroupSetHibernateDomainBoundary entityBoundary,
            HandicapGroupSetHibernateAddPayloadBoundary addPayloadBoundary,
            HandicapGroupSetDao dao) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
