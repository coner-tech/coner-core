package org.coner.core.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Run extends DomainEntity {
    private String id;
    private Event event;
    private Registration registration;
    private int sequence;
    private Instant timestamp;
    private BigDecimal rawTime;
    private int cones;
    private boolean didNotFinish;
    private boolean disqualified;
    private boolean rerun;
    private boolean competitive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isDidNotFinish() {
        return didNotFinish;
    }

    public void setDidNotFinish(boolean didNotFinish) {
        this.didNotFinish = didNotFinish;
    }

    public boolean isDisqualified() {
        return disqualified;
    }

    public void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
