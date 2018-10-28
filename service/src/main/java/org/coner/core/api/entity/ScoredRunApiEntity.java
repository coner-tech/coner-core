package org.coner.core.api.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ScoredRunApiEntity {

    private String runId;
    private BigDecimal rawTimeScored;
    private BigDecimal handicapTimeScored;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public BigDecimal getRawTimeScored() {
        return rawTimeScored;
    }

    public void setRawTimeScored(BigDecimal rawTimeScored) {
        this.rawTimeScored = rawTimeScored;
    }

    public BigDecimal getHandicapTimeScored() {
        return handicapTimeScored;
    }

    public void setHandicapTimeScored(BigDecimal handicapTimeScored) {
        this.handicapTimeScored = handicapTimeScored;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
