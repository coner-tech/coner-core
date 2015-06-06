package org.coner.core.domain;

import java.util.Set;

/**
 * Domain entity for the representation of HandicapGroupSets.
 */
public class HandicapGroupSet extends DomainEntity {

    private String id;
    private String name;
    private Set<HandicapGroup> handicapGroups;

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

    public Set<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
