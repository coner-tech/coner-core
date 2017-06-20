package org.coner.core.domain.entity;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Event extends DomainEntity {

    private String id;
    private String name;
    private Date date;
    private HandicapGroupSet handicapGroupSet;
    private CompetitionGroupSet competitionGroupSet;
    private int maxRunsPerRegistration;
    private boolean running;

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

    public boolean hasDate() {
        return date != null;
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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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
