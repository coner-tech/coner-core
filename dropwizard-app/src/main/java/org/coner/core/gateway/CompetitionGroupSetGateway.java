package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupSetBoundary;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.dao.CompetitionGroupSetDao;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

public class CompetitionGroupSetGateway extends AbstractGateway<
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetBoundary,
        CompetitionGroupSetDao> {

    public CompetitionGroupSetGateway(CompetitionGroupSetBoundary boundary, CompetitionGroupSetDao dao) {
        super(boundary, dao);
    }
}
