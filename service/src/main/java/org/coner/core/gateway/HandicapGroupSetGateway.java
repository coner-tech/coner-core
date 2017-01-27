package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.boundary.HandicapGroupSetHibernateAddPayloadBoundary;
import org.coner.core.boundary.HandicapGroupSetHibernateDomainBoundary;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.hibernate.entity.HandicapGroupSetHibernateEntity;

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
