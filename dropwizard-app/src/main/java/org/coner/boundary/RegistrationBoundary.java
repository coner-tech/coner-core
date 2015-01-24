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

    private static RegistrationBoundary instance;

    /**
     * Get the singleton instance of the RegistrationBoundary.
     *
     * @return a RegistrationBoundary
     */
    public static RegistrationBoundary getInstance() {
        if (instance == null) {
            instance = new RegistrationBoundary();
        }
        return instance;
    }

    public static void setInstance(RegistrationBoundary rb) {
        instance = rb;
    }

    private EventBoundary eventBoundary;

    /**
     * Private constructor called only by `RegistrationBoundary.getInstance`. Calls the package-private constructor
     * using the singleton instance of EventBoundary.
     */
    private RegistrationBoundary() {
        this(EventBoundary.getInstance());
    }

    /**
     * Package-private constructor which should only be called by the private constructor or a test.
     *
     * @param eventBoundary the EventBoundary to use when converting Event entities
     */
    RegistrationBoundary(EventBoundary eventBoundary) {
        super(
                org.coner.api.entity.Registration.class,
                Registration.class,
                org.coner.hibernate.entity.Registration.class
        );
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
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> destinationEntity.setEvent(
                eventBoundary.toHibernateEntity(sourceEntity.getEvent())
        ));
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.Registration, Registration> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((sourceEntity, destinationEntity) -> {
            destinationEntity.setEvent(eventBoundary.toDomainEntity(sourceEntity.getEvent()));
        });
    }
}
