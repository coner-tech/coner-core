package org.coner.core.domain.payload;

import java.math.BigDecimal;
import java.util.Objects;

public class CompetitionGroupAddPayload extends DomainAddPayload {
    private String name;
    private BigDecimal handicapFactor;
    private Boolean grouping;
    private String resultTimeType;

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

    public Boolean getGrouping() {
        return grouping;
    }

    public void setGrouping(Boolean grouping) {
        this.grouping = grouping;
    }

    public String getResultTimeType() {
        return resultTimeType;
    }

    public void setResultTimeType(String resultTimeType) {
        this.resultTimeType = resultTimeType;
    }

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
