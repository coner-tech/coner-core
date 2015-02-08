package org.coner.boundary;

import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;

/**
 * Converts Registration entities as they cross architectural boundaries.
 */
public class RegistrationBoundary extends AbstractBoundary<
        org.coner.api.entity.Registration,
        Registration,
        org.coner.hibernate.entity.Registration> {

    private final EventBoundary eventBoundary;

    /**
     * Public constructor for the RegistrationBoundary
     *
     * @param eventBoundary the EventBoundary to use when converting Event entities
     */
    public RegistrationBoundary(EventBoundary eventBoundary) {
        super();
        this.eventBoundary = eventBoundary;
    }

    @Override
    protected EntityMerger<org.coner.api.entity.Registration, Registration> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            // When Api => Domain, the event could be null
            Event domainEvent = new Event();
            if (sourceEntity.getEvent() != null) {
                domainEvent.setId(sourceEntity.getEvent().getId());
            }
            destinationEntity.setEvent(domainEvent);
        });
    }

    @Override
    protected EntityMerger<Registration, org.coner.api.entity.Registration> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            org.coner.api.entity.Registration.Event event = new org.coner.api.entity.Registration.Event();
            event.setId(sourceEntity.getEvent().getId());
            destinationEntity.setEvent(event);
        });
    }

    @Override
    protected EntityMerger<Registration, org.coner.hibernate.entity.Registration> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            destinationEntity.setEvent(eventBoundary.toHibernateEntity(sourceEntity.getEvent()));
        });
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.Registration, Registration> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            destinationEntity.setEvent(eventBoundary.toDomainEntity(sourceEntity.getEvent()));
        });
    }
}
