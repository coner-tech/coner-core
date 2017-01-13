package org.coner.api.request;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class AddHandicapGroupRequest {

    @NotBlank
    private String name;
    @NotNull
    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
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
        AddHandicapGroupRequest that = (AddHandicapGroupRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(handicapFactor, that.handicapFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, handicapFactor);
    }
}
