package org.coner.core.domain.payload;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.entity.HandicapGroupSet;

public class EventAddPayload extends DomainAddPayload {

    private String name;
    private Date date;
    private HandicapGroupSet handicapGroupSet;
    private CompetitionGroupSet competitionGroupSet;
    private int maxRunsPerRegistration;
    // intentionally omitting domain entity property: current

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

    public HandicapGroupSet getHandicapGroupSet() {
        return handicapGroupSet;
    }

    public void setHandicapGroupSet(HandicapGroupSet handicapGroupSet) {
        this.handicapGroupSet = handicapGroupSet;
    }

    public CompetitionGroupSet getCompetitionGroupSet() {
        return competitionGroupSet;
    }

    public void setCompetitionGroupSet(CompetitionGroupSet competitionGroupSet) {
        this.competitionGroupSet = competitionGroupSet;
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
