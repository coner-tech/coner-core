package org.coner.core.domain.payload;

import java.util.Date;
import java.util.Objects;

public class EventAddPayload extends DomainAddPayload {
    public String name;
    public Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAddPayload that = (EventAddPayload) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }
}
