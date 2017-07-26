package org.coner.core.domain.entity;

import java.math.BigDecimal;

public class ScoredRun extends DomainEntity {

    private Run run;
    private BigDecimal rawTimeScored;
    private BigDecimal handicapTimeScored;

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
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
}
