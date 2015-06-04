package org.coner.boundary;

import org.coner.core.domain.Event;

public class EventBoundary extends AbstractBoundary<
        org.coner.api.entity.Event,
        Event,
        org.coner.hibernate.entity.Event> {

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
