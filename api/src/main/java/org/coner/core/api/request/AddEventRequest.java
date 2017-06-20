package org.coner.core.api.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotBlank;

public class AddEventRequest {

    @NotBlank
    private String name;
    @NotNull
    private Date date;
    @NotBlank
    private String handicapGroupSetId;
    @NotBlank
    private String competitionGroupSetId;
    @NotNull
    private int maxRunsPerRegistration;
    // intentionally omitting api entity property: running

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

    public int getMaxRunsPerRegistration() {
        return maxRunsPerRegistration;
    }

    public void setMaxRunsPerRegistration(int maxRunsPerRegistration) {
        this.maxRunsPerRegistration = maxRunsPerRegistration;
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
