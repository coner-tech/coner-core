package org.coner.boundary;

import org.coner.api.entity.RegistrationApiEntity;
import org.coner.core.domain.Registration;
import org.coner.hibernate.entity.RegistrationHibernateEntity;

public class RegistrationBoundary extends AbstractBoundary<
        RegistrationApiEntity,
        Registration,
        RegistrationHibernateEntity> {

    private final EventBoundary eventBoundary;

    public RegistrationBoundary(EventBoundary eventBoundary) {
        super();
        this.eventBoundary = eventBoundary;
    }

    @Override
    protected EntityMerger<RegistrationApiEntity, Registration> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Registration, RegistrationApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Registration, RegistrationHibernateEntity> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            destinationEntity.setEvent(eventBoundary.toHibernateEntity(sourceEntity.getEvent()));
        });
    }

    @Override
    protected EntityMerger<RegistrationHibernateEntity, Registration> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            destinationEntity.setEvent(eventBoundary.toDomainEntity(sourceEntity.getEvent()));
        });
    }
}
