package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupBoundary;
import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

public class HandicapGroupGateway extends AbstractGateway<
        HandicapGroup,
        HandicapGroupHibernateEntity,
        HandicapGroupBoundary,
        HandicapGroupDao> {

    public HandicapGroupGateway(HandicapGroupBoundary boundary, HandicapGroupDao dao) {
        super(boundary, dao);
    }
}
