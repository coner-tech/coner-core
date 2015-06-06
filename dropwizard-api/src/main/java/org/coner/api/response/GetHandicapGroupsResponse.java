package org.coner.api.response;

import org.coner.api.entity.HandicapGroupApiEntity;

import java.util.List;

public class GetHandicapGroupsResponse {
    private List<HandicapGroupApiEntity> handicapGroups;

    public List<HandicapGroupApiEntity> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(List<HandicapGroupApiEntity> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
