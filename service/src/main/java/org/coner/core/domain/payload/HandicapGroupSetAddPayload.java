package org.coner.core.domain.payload;

import java.util.Objects;
import java.util.Set;

import org.coner.core.domain.entity.HandicapGroup;

public class HandicapGroupSetAddPayload extends DomainAddPayload  {
    private String name;
    private Set<String> handicapGroupIds;
    private Set<HandicapGroup> handicapGroups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getHandicapGroupIds() {
        return handicapGroupIds;
    }

    public void setHandicapGroupIds(Set<String> handicapGroupIds) {
        this.handicapGroupIds = handicapGroupIds;
    }

    public Set<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandicapGroupSetAddPayload that = (HandicapGroupSetAddPayload) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(handicapGroupIds, that.handicapGroupIds) &&
                Objects.equals(handicapGroups, that.handicapGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, handicapGroupIds, handicapGroups);
    }
}
