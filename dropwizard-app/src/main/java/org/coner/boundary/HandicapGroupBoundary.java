package org.coner.boundary;

import org.coner.api.entity.HandicapGroupApiEntity;
import org.coner.core.domain.HandicapGroup;
import org.coner.hibernate.entity.HandicapGroupHibernateEntity;

public class HandicapGroupBoundary extends AbstractBoundary<
        HandicapGroupApiEntity,
        HandicapGroup,
        HandicapGroupHibernateEntity> {

    @Override
    protected EntityMerger<HandicapGroupApiEntity, HandicapGroup> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, HandicapGroupApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroup, HandicapGroupHibernateEntity> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<HandicapGroupHibernateEntity, HandicapGroup> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }
}
