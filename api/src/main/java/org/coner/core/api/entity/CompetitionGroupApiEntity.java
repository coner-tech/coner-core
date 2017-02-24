package org.coner.core.api.entity;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class CompetitionGroupApiEntity extends ApiEntity {

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
        CompetitionGroupApiEntity that = (CompetitionGroupApiEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.compare(handicapFactor, that.handicapFactor, BigDecimal::compareTo) == 0 &&
                Objects.equals(grouping, that.grouping) &&
                Objects.equals(resultTimeType, that.resultTimeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, handicapFactor, grouping, resultTimeType);
    }
}
