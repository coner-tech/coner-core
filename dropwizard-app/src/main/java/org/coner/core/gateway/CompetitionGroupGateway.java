package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupHibernateDomainBoundary;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupGateway extends AbstractGateway<
        CompetitionGroup,
        CompetitionGroupHibernateEntity,
        CompetitionGroupHibernateDomainBoundary,
        CompetitionGroupDao> {

    public CompetitionGroupGateway(CompetitionGroupHibernateDomainBoundary boundary, CompetitionGroupDao dao) {
        super(boundary, dao);
    }
}
