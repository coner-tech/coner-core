package org.coner.core.domain.service;

import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.gateway.CompetitionGroupSetGateway;

public class CompetitionGroupSetService extends AbstractDomainService<
        CompetitionGroupSet,
        CompetitionGroupSetGateway> {
    public CompetitionGroupSetService(CompetitionGroupSetGateway gateway) {
        super(gateway);
    }
}
