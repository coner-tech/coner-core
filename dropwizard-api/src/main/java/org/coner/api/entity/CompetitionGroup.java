package org.coner.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * REST API entity representing a Competition Group.
 */
@JsonPropertyOrder({"id", "name", "handicapFactor", "grouping", "resultTimeType"})
public class CompetitionGroup extends ApiEntity {

    @Null(message = "competitionGroup.id may only be assigned by the system")
    private String id;
    @NotBlank
    private String name;
    @NotNull
    @DecimalMin("0.000")
    @DecimalMax("1.000")
    private BigDecimal handicapFactor;
    @NotNull
    private Boolean grouping;
    @NotBlank
    private String resultTimeType;

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

    public Boolean isGrouping() {
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

        CompetitionGroup that = (CompetitionGroup) o;

        if (grouping != null ? !grouping.equals(that.grouping) : that.grouping != null) return false;
        if (handicapFactor != null ? !handicapFactor.equals(that.handicapFactor) : that.handicapFactor != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (resultTimeType != null ? !resultTimeType.equals(that.resultTimeType) : that.resultTimeType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (handicapFactor != null ? handicapFactor.hashCode() : 0);
        result = 31 * result + (grouping != null ? grouping.hashCode() : 0);
        result = 31 * result + (resultTimeType != null ? resultTimeType.hashCode() : 0);
        return result;
    }
}
