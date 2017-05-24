package org.coner.core.domain.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CompetitionGroup extends DomainEntity {

    private String id;
    private String name;
    private BigDecimal factor;
    private boolean grouping;
    private ResultTimeType resultTimeType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
    }

    public ResultTimeType getResultTimeType() {
        return resultTimeType;
    }

    public void setResultTimeType(ResultTimeType resultTimeType) {
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

    /**
     * Indicates which result time type should be used to rank results pertaining to a competition group.
     */
    public enum ResultTimeType {
        HANDICAP,
        RAW
    }
}
