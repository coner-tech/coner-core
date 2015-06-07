package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupBoundary;
import org.coner.core.domain.CompetitionGroup;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupGateway extends AbstractGateway<
        CompetitionGroup,
        CompetitionGroupHibernateEntity,
        CompetitionGroupBoundary,
        CompetitionGroupDao> {

    public CompetitionGroupGateway(CompetitionGroupBoundary boundary, CompetitionGroupDao dao) {
        super(boundary, dao);
    }
}
