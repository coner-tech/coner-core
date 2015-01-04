package org.coner.boundary;

import org.coner.core.domain.Event;

import java.util.Date;

/**
 * Converts Event entities as they cross architectural boundaries
 */
public class EventBoundary extends AbstractBoundary<
        org.coner.api.entity.Event,
        Event,
        org.coner.hibernate.entity.Event> {

    static EventBoundary instance;

    /**
     * Get the singleton instance of the EventBoundary.
     *
     * @return an EventBoundary
     */
    public static EventBoundary getInstance() {
        if (instance == null) {
            instance = new EventBoundary();
        }
        return instance;
    }

    /**
     * Package-private constructor which should only ever be called by `EventBoundary.getInstance` or a test
     */
    EventBoundary() {
        super(
                org.coner.api.entity.Event.class,
                Event.class,
                org.coner.hibernate.entity.Event.class
        );
    }

    @Override
    protected EntityMerger<org.coner.api.entity.Event, Event> buildApiToDomainMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setEventId(fromEntity.getId());
            toEntity.setDate(new Date(fromEntity.getDate().getTime()));
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<Event, org.coner.api.entity.Event> buildDomainToApiMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getEventId());
            toEntity.setDate(new Date(fromEntity.getDate().getTime()));
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<Event, org.coner.hibernate.entity.Event> buildDomainToHibernateMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getEventId());
            toEntity.setDate(new Date(fromEntity.getDate().getTime()));
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.Event, Event> buildHibernateToDomainMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setEventId(fromEntity.getId());
            toEntity.setDate(new Date(fromEntity.getDate().getTime()));
            toEntity.setName(fromEntity.getName());
        };
    }
}
