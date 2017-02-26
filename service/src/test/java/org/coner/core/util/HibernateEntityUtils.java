package org.coner.core.util;

import java.util.Date;

import org.coner.core.hibernate.entity.EventHibernateEntity;

/**
 *
 */
public final class HibernateEntityUtils {

    private HibernateEntityUtils() {
    }

    public static EventHibernateEntity fullEvent() {
        return fullEvent(TestConstants.EVENT_ID, TestConstants.EVENT_NAME, TestConstants.EVENT_DATE);
    }

    public static EventHibernateEntity fullEvent(String id, String name, Date date) {
        EventHibernateEntity event = new EventHibernateEntity();
        event.setId(id);
        event.setName(name);
        event.setDate(date);
        return event;
    }
}
