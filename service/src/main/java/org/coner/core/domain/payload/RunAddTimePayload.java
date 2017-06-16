package org.coner.core.domain.payload;

import java.math.BigDecimal;

import org.coner.core.domain.entity.Event;

public class RunAddTimePayload extends DomainAddPayload {

    private Event event;
    private BigDecimal rawTime;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public BigDecimal getRawTime() {
        return rawTime;
    }

    public void setRawTime(BigDecimal rawTime) {
        this.rawTime = rawTime;
    }
}
