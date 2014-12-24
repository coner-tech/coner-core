package org.axrunner.boundary;

import org.axrunner.core.domain.Event;

/**
 *
 */
public class EventBoundary extends AbstractBoundary<org.axrunner.api.entity.Event, Event, org.axrunner.hibernate.entity.Event> {


    public EventBoundary() {
        super(
                org.axrunner.api.entity.Event.class,
                Event.class,
                org.axrunner.hibernate.entity.Event.class
        );
    }

    @Override
    public void merge(org.axrunner.api.entity.Event fromApiEntity, Event intoDomainEntity) {
        intoDomainEntity.setEventId(fromApiEntity.getId());
        intoDomainEntity.setDate(fromApiEntity.getDate());
        intoDomainEntity.setName(fromApiEntity.getName());
    }

    @Override
    public void merge(Event fromDomainEntity, org.axrunner.api.entity.Event intoApiEntity) {
        intoApiEntity.setId(fromDomainEntity.getEventId());
        intoApiEntity.setDate(fromDomainEntity.getDate());
        intoApiEntity.setName(fromDomainEntity.getName());
    }

    @Override
    public void merge(org.axrunner.hibernate.entity.Event fromHibernateEntity, Event intoDomainEntity) {
        intoDomainEntity.setEventId(String.valueOf(fromHibernateEntity.getId()));
        intoDomainEntity.setDate(fromHibernateEntity.getDate());
        intoDomainEntity.setName(fromHibernateEntity.getName());
    }

    @Override
    public void merge(Event fromDomainEntity, org.axrunner.hibernate.entity.Event intoHibernateEntity) {
        intoHibernateEntity.setDate(fromDomainEntity.getDate());
        intoHibernateEntity.setName(fromDomainEntity.getName());
    }
}
