package org.coner.core.api.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class EventApiEntity extends ApiEntity {

    @Null(message = "event.id may only be assigned by the system")
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Date date;
    @NotBlank
    private String handicapGroupSetId;
    @NotBlank
    private String competitionGroupSetId;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHandicapGroupSetId() {
        return handicapGroupSetId;
    }

    public void setHandicapGroupSetId(String handicapGroupSetId) {
        this.handicapGroupSetId = handicapGroupSetId;
    }

    public String getCompetitionGroupSetId() {
        return competitionGroupSetId;
    }

    public void setCompetitionGroupSetId(String competitionGroupSetId) {
        this.competitionGroupSetId = competitionGroupSetId;
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
