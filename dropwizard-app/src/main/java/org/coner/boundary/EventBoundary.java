package org.coner.boundary;

import org.coner.core.domain.Event;

/**
 * Converts Event entities as they cross architectural boundaries.
 */
public class EventBoundary extends AbstractBoundary<
        org.coner.api.entity.Event,
        Event,
        org.coner.hibernate.entity.Event> {

    private static EventBoundary instance;

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

    public static void setInstance(EventBoundary eventBoundary) {
        instance = eventBoundary;
    }

    /**
     * Package-private constructor which should only ever be called by `EventBoundary.getInstance` or a test.
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
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, org.coner.api.entity.Event> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<Event, org.coner.hibernate.entity.Event> buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.Event, Event> buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }
}
