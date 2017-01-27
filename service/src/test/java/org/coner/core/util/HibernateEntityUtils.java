package org.coner.core.util;

import java.util.Date;

import org.coner.core.hibernate.entity.EventHibernateEntity;

/**
 *
 */
public final class HibernateEntityUtils {

    private HibernateEntityUtils() {
    }

    public static EventHibernateEntity fullHibernateEvent() {
        return fullHibernateEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static EventHibernateEntity fullHibernateEvent(String eventId, String eventName, Date eventDate) {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setId(eventId);
        event.setName(eventName);
        event.setDate(eventDate);
        return event;
    }
}
