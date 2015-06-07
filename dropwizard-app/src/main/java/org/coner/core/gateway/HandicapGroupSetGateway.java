package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupSetBoundary;
import org.coner.core.domain.HandicapGroupSet;
import org.coner.hibernate.dao.HandicapGroupSetDao;
import org.coner.hibernate.entity.HandicapGroupSetHibernateEntity;

public class HandicapGroupSetGateway extends AbstractGateway<
        HandicapGroupSet,
        HandicapGroupSetHibernateEntity,
        HandicapGroupSetBoundary,
        HandicapGroupSetDao> {

    public HandicapGroupSetGateway(HandicapGroupSetBoundary boundary, HandicapGroupSetDao dao) {
        super(boundary, dao);
    }
}
