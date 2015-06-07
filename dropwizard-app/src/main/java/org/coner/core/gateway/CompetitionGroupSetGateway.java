package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupSetHibernateDomainBoundary;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.dao.CompetitionGroupSetDao;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

public class CompetitionGroupSetGateway extends AbstractGateway<
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetHibernateDomainBoundary,
        CompetitionGroupSetDao> {

    public CompetitionGroupSetGateway(CompetitionGroupSetHibernateDomainBoundary boundary, CompetitionGroupSetDao dao) {
        super(boundary, dao);
    }
}
