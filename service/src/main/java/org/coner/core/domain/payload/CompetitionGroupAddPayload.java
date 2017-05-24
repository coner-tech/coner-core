package org.coner.core.domain.payload;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CompetitionGroupAddPayload extends DomainAddPayload {
    private String name;
    private BigDecimal factor;
    private Boolean grouping;
    private String resultTimeType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
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
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
