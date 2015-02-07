package org.coner.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * REST API entity representing a Competition Group
 */
@JsonPropertyOrder({"id", "name", "handicapFactor", "grouping", "resultTimeType"})
public class CompetitionGroup extends ApiEntity {

    @Null(message = "competitionGroup.id may only be assigned by the system")
    private String id;
    @NotBlank
    private String name;
    private BigDecimal handicapFactor;
    private boolean grouping;
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

    public boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
    }

    public String getResultTimeType() {
        return resultTimeType;
    }

    public void setResultTimeType(String resultTimeType) {
        this.resultTimeType = resultTimeType;
    }
}
