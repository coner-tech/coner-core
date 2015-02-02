package org.coner.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * REST API entity representing a HandicapGroup.
 */
@JsonPropertyOrder({"id", "name", "handicapFactor"})
public class HandicapGroup extends ApiEntity {

    @Null(message = "handicapGroup.id may only be assigned by the system")
    private String id;
    @NotNull
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

        HandicapGroup that = (HandicapGroup) o;

        if (handicapFactor != null ? !handicapFactor.equals(that.handicapFactor) : that.handicapFactor != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (handicapFactor != null ? handicapFactor.hashCode() : 0);
        return result;
    }
}
