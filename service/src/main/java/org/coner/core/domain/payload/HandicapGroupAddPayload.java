package org.coner.core.domain.payload;

import java.math.BigDecimal;
import java.util.Objects;

public class HandicapGroupAddPayload extends DomainAddPayload {

    private String name;
    private BigDecimal handicapFactor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getHandicapFactor() {
        return handicapFactor;
    }

    public void setHandicapFactor(BigDecimal handicapFactor) {
        this.handicapFactor = handicapFactor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandicapGroupAddPayload that = (HandicapGroupAddPayload) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(handicapFactor, that.handicapFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, handicapFactor);
    }
}
