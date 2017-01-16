package org.coner.core.gateway;

import javax.inject.Inject;

import org.coner.boundary.CompetitionGroupSetHibernateAddPayloadBoundary;
import org.coner.boundary.CompetitionGroupSetHibernateDomainBoundary;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.hibernate.dao.CompetitionGroupSetDao;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

public class CompetitionGroupSetGateway extends AbstractGateway<
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity,
        CompetitionGroupSetAddPayload,
        CompetitionGroupSetHibernateDomainBoundary,
        CompetitionGroupSetHibernateAddPayloadBoundary,
        CompetitionGroupSetDao> {

    @Inject
    public CompetitionGroupSetGateway(
            CompetitionGroupSetHibernateDomainBoundary entityBoundary,
            CompetitionGroupSetHibernateAddPayloadBoundary addPayloadBoundary,
            CompetitionGroupSetDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
