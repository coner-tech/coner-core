package org.coner.api.response;

import org.coner.api.entity.HandicapGroup;

import java.util.List;

/**
 * API response object containing a list of HandicapGroup entities.
 */
public class GetHandicapGroupsResponse {
    private List<HandicapGroup> handicapGroups;

    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(List<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
