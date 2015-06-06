package org.coner.api.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Set;

/**
 * REST API entity representing a Handicap Group Set
 */
@JsonPropertyOrder({"id", "name", "handicapGroups"})
public class HandicapGroupSet extends ApiEntity {

    private String id;
    private String name;
    private Set<HandicapGroupApiEntity> handicapGroups;

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

    public Set<HandicapGroupApiEntity> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(Set<HandicapGroupApiEntity> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
