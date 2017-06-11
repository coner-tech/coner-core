package org.coner.core.domain.payload;

import java.math.BigDecimal;
import java.time.Instant;

import org.coner.core.domain.entity.Event;
import org.coner.core.domain.entity.Registration;

public class RunAddPayload extends DomainAddPayload {

    private Event event;
    private Registration registration;
    private int sequence;
    private Instant timestamp;
    private BigDecimal rawTime;
    private int cones;
    private String penalty;
    private boolean rerun;
    private boolean competitive;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getRawTime() {
        return rawTime;
    }

    public void setRawTime(BigDecimal rawTime) {
        this.rawTime = rawTime;
    }

    public int getCones() {
        return cones;
    }

    public void setCones(int cones) {
        this.cones = cones;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public boolean isRerun() {
        return rerun;
    }

    public void setRerun(boolean rerun) {
        this.rerun = rerun;
    }

    public boolean isCompetitive() {
        return competitive;
    }

    public void setCompetitive(boolean competitive) {
        this.competitive = competitive;
    }
}
