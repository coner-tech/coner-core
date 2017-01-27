package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.core.boundary.CompetitionGroupHibernateAddPayloadBoundary;
import org.coner.core.boundary.CompetitionGroupHibernateDomainBoundary;
import org.coner.core.api.entity.CompetitionGroup;
import org.coner.core.domain.payload.CompetitionGroupAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.entity.CompetitionGroupHibernateEntity;

public class CompetitionGroupGateway extends AbstractGateway<
        CompetitionGroup,
        CompetitionGroupHibernateEntity,
        CompetitionGroupAddPayload,
        CompetitionGroupHibernateDomainBoundary,
        CompetitionGroupHibernateAddPayloadBoundary,
        CompetitionGroupDao> {

    @Inject
    public CompetitionGroupGateway(
            CompetitionGroupHibernateDomainBoundary entityBoundary,
            CompetitionGroupHibernateAddPayloadBoundary addPayloadBoundary,
            CompetitionGroupDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
