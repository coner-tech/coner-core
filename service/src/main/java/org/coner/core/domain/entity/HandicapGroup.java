package org.coner.core.domain.entity;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HandicapGroup extends DomainEntity {
    private String id;
    private String name;
    private BigDecimal handicapFactor;

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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
