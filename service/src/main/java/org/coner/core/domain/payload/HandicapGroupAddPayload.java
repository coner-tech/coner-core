package org.coner.core.domain.payload;

import java.math.BigDecimal;
import java.util.Objects;

public class HandicapGroupAddPayload extends DomainAddPayload {

    public String name;
    public BigDecimal handicapFactor;

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
