package org.coner.core.api.response;

import java.util.List;

import org.coner.core.api.entity.HandicapGroupApiEntity;

public class GetHandicapGroupsResponse {
    private List<HandicapGroupApiEntity> handicapGroups;

    public List<HandicapGroupApiEntity> getHandicapGroups() {
        return handicapGroups;
    }

    public void setHandicapGroups(List<HandicapGroupApiEntity> handicapGroups) {
        this.handicapGroups = handicapGroups;
    }
}
