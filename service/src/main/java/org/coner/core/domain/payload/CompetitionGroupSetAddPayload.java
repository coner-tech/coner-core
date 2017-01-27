package org.coner.core.domain.payload;

import java.util.Set;

import org.coner.core.api.entity.CompetitionGroup;

public class CompetitionGroupSetAddPayload extends DomainAddPayload {
    public String name;
    public Set<String> competitionGroupIds;
    public Set<CompetitionGroup> competitionGroups;
}
