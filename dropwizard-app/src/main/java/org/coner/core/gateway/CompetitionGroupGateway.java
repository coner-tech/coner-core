package org.coner.core.gateway;

import org.coner.boundary.CompetitionGroupHibernateAddPayloadBoundary;
import org.coner.boundary.CompetitionGroupHibernateDomainBoundary;
import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.hibernate.dao.CompetitionGroupDao;
import org.coner.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupGateway extends AbstractGateway<
        CompetitionGroup,
        CompetitionGroupHibernateEntity,
        CompetitionGroupAddPayload,
        CompetitionGroupHibernateDomainBoundary,
        CompetitionGroupHibernateAddPayloadBoundary,
        CompetitionGroupDao> {

    public CompetitionGroupGateway(
            CompetitionGroupHibernateDomainBoundary entityBoundary,
            CompetitionGroupHibernateAddPayloadBoundary addPayloadBoundary,
            CompetitionGroupDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
