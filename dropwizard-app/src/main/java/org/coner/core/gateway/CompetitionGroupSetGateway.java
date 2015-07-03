package org.coner.core.gateway;

import org.coner.boundary.*;
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

    public CompetitionGroupSetGateway(
            CompetitionGroupSetHibernateDomainBoundary entityBoundary,
            CompetitionGroupSetHibernateAddPayloadBoundary addPayloadBoundary,
            CompetitionGroupSetDao dao
    ) {
        super(entityBoundary, addPayloadBoundary, dao);
    }
}
