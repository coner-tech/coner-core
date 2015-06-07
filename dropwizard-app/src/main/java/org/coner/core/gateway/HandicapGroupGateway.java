package org.coner.core.gateway;

import org.coner.boundary.HandicapGroupHibernateDomainBoundary;
import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.dao.HandicapGroupDao;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

public class HandicapGroupGateway extends AbstractGateway<
        HandicapGroup,
        HandicapGroupHibernateEntity,
        HandicapGroupHibernateDomainBoundary,
        HandicapGroupDao> {

    public HandicapGroupGateway(HandicapGroupHibernateDomainBoundary boundary, HandicapGroupDao dao) {
        super(boundary, dao);
    }
}
