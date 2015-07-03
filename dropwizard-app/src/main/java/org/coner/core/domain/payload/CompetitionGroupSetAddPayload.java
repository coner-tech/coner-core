package org.coner.core.domain.payload;

import org.coner.core.domain.entity.CompetitionGroup;

import java.util.Set;

public class CompetitionGroupSetAddPayload extends DomainAddPayload {
    public String name;
    public Set<String> competitionGroupIds;
    public Set<CompetitionGroup> competitionGroups;
}
