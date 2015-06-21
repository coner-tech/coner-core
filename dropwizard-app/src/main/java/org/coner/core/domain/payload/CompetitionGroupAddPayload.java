package org.coner.core.domain.payload;

import java.math.BigDecimal;
import java.util.Objects;

public class CompetitionGroupAddPayload extends DomainAddPayload {
    public String name;
    public BigDecimal handicapFactor;
    public Boolean grouping;
    public String resultTimeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompetitionGroupAddPayload that = (CompetitionGroupAddPayload) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(handicapFactor, that.handicapFactor) &&
                Objects.equals(grouping, that.grouping) &&
                Objects.equals(resultTimeType, that.resultTimeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, handicapFactor, grouping, resultTimeType);
    }
}
