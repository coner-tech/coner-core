package org.coner.core.domain.service;

import org.coner.core.domain.entity.CompetitionGroup;
import org.coner.core.gateway.CompetitionGroupGateway;

public class CompetitionGroupService extends AbstractDomainService<CompetitionGroup, CompetitionGroupGateway> {
    public CompetitionGroupService(CompetitionGroupGateway gateway) {
        super(gateway);
    }
}
