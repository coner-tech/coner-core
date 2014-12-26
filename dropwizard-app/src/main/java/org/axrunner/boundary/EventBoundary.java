package org.axrunner.boundary;

import org.axrunner.core.domain.Event;

/**
 *
 */
public class EventBoundary extends AbstractBoundary<org.axrunner.api.entity.Event, Event, org.axrunner.hibernate.entity.Event> {

    private static EventBoundary instance;

    public static EventBoundary getInstance() {
        if (instance == null) {
            instance = new EventBoundary();
        }
        return instance;
    }

    EventBoundary() {
        super(
                org.axrunner.api.entity.Event.class,
                Event.class,
                org.axrunner.hibernate.entity.Event.class
        );
    }

    @Override
    protected EntityMerger<org.axrunner.api.entity.Event, Event> buildApiToDomainMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setEventId(fromEntity.getId());
            toEntity.setDate(fromEntity.getDate());
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<Event, org.axrunner.api.entity.Event> buildDomainToApiMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setId(fromEntity.getEventId());
            toEntity.setDate(fromEntity.getDate());
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<Event, org.axrunner.hibernate.entity.Event> buildDomainToHibernateMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setDate(fromEntity.getDate());
            toEntity.setName(fromEntity.getName());
        };
    }

    @Override
    protected EntityMerger<org.axrunner.hibernate.entity.Event, Event> buildHibernateToDomainMerger() {
        return (fromEntity, toEntity) -> {
            toEntity.setEventId(String.valueOf(fromEntity.getId()));
            toEntity.setDate(fromEntity.getDate());
            toEntity.setName(fromEntity.getName());
        };
    }
}
