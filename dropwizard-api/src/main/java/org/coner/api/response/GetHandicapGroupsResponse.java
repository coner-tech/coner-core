package org.coner.api.response;

import org.coner.api.entity.HandicapGroup;

import java.util.List;

public class GetHandicapGroupsResponse {
    private List<HandicapGroup> handicapGroups;

    public List<HandicapGroup> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(List<HandicapGroup> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
