package org.coner.core.domain.payload;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.coner.core.domain.entity.HandicapGroup;

public class HandicapGroupSetAddPayload extends DomainAddPayload {
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
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
