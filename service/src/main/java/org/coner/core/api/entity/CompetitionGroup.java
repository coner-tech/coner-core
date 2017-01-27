package org.coner.core.api.entity;

import java.math.BigDecimal;

public class CompetitionGroup extends DomainEntity {

    private String id;
    private String name;
    private BigDecimal handicapFactor;
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

    public BigDecimal getHandicapFactor() {
        return handicapFactor;
    }

    public void setHandicapFactor(BigDecimal handicapFactor) {
        this.handicapFactor = handicapFactor;
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

    /**
     * Indicates which result time type should be used to rank results pertaining to a competition group.
     */
    public enum ResultTimeType {
        HANDICAP,
        RAW
    }
}
