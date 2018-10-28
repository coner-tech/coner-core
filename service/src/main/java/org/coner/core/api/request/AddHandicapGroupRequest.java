package org.coner.core.api.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddHandicapGroupRequest {

    @NotBlank
    private String name;
    @NotNull
    @DecimalMin("0.000")
    @DecimalMax("1.000")
    private BigDecimal factor;

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

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
