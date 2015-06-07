package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupSetHibernateDomainBoundary;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.dao.HandicapGroupSetDao;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

public class HandicapGroupSetGateway extends AbstractGateway<
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetHibernateDomainBoundary,
        HandicapGroupSetDao> {

    public HandicapGroupSetGateway(HandicapGroupSetHibernateDomainBoundary boundary, HandicapGroupSetDao dao) {
        super(boundary, dao);
    }
}
