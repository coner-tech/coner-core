package org.coner.boundary;

import org.coner.core.domain.Event;
import org.coner.core.domain.Registration;

/**
 *
 */
public class RegistrationBoundary extends AbstractBoundary<org.coner.api.entity.Registration, Registration, org.coner.hibernate.entity.Registration> {

    private static RegistrationBoundary instance;

    public static RegistrationBoundary getInstance() {
        if (instance == null) {
            instance = new RegistrationBoundary();
        }
        return instance;
    }

    private EventBoundary eventBoundary;

    private RegistrationBoundary() {
        this(EventBoundary.getInstance());
    }

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
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getId());
            toEntity.setFirstName(fromEntity.getFirstName());
            toEntity.setLastName(fromEntity.getLastName());
            Event domainEvent = new Event();
            domainEvent.setEventId(fromEntity.getEvent().getId());
            toEntity.setEvent(domainEvent);
        };
    }

    @Override
    protected EntityMerger<Registration, org.coner.api.entity.Registration> buildDomainToApiMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getId());
            toEntity.setFirstName(fromEntity.getFirstName());
            toEntity.setLastName(fromEntity.getLastName());
            org.coner.api.entity.Registration.Event event = new org.coner.api.entity.Registration.Event();
            event.setId(fromEntity.getId());
        };
    }

    @Override
    protected EntityMerger<Registration, org.coner.hibernate.entity.Registration> buildDomainToHibernateMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setFirstName(fromEntity.getFirstName());
            toEntity.setLastName(fromEntity.getLastName());
            toEntity.setEvent(eventBoundary.toHibernateEntity(fromEntity.getEvent()));
        };
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.Registration, Registration> buildHibernateToDomainMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getId());
            toEntity.setFirstName(fromEntity.getFirstName());
            toEntity.setLastName(fromEntity.getLastName());
            toEntity.setEvent(eventBoundary.toDomainEntity(fromEntity.getEvent()));
        };
    }
}
