package org.coner.api.entity;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name", "handicapFactor"})
public class HandicapGroupApiEntity extends ApiEntity {

    private String id;
    @NotBlank
    private String name;
    @NotNull
    @DecimalMin("0.0000")
    @DecimalMax("1.0000")
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandicapGroupApiEntity apiEntity = (HandicapGroupApiEntity) o;
        return Objects.equals(id, apiEntity.id) &&
                Objects.equals(name, apiEntity.name) &&
                Objects.equals(handicapFactor, apiEntity.handicapFactor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handicapFactor);
    }
}
