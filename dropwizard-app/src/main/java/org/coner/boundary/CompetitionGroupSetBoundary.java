package org.coner.boundary;

import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.CompetitionGroupSet;

public class CompetitionGroupSetBoundary extends AbstractBoundary<
        org.coner.api.entity.CompetitionGroupSet,
        CompetitionGroupSet,
        org.coner.hibernate.entity.CompetitionGroupSet
        > {

    private EntityMerger<org.coner.api.request.AddCompetitionGroupSetRequest, CompetitionGroupSet>
            apiAddCompetitionGroupSetRequestToDomainCompetitionGroupSetEntityMerger;

    @Override
    protected EntityMerger<org.coner.api.entity.CompetitionGroupSet, CompetitionGroupSet> buildApiToDomainMerger() {
        return new ReflectionEntityMerger<>();
    }

    public CompetitionGroupSet toDomainEntity(AddCompetitionGroupSetRequest addCompetitionGroupSetRequest) {
        if (apiAddCompetitionGroupSetRequestToDomainCompetitionGroupSetEntityMerger == null) {
            apiAddCompetitionGroupSetRequestToDomainCompetitionGroupSetEntityMerger = new ReflectionEntityMerger<>();
        }
        CompetitionGroupSet competitionGroupSet = new CompetitionGroupSet();
        apiAddCompetitionGroupSetRequestToDomainCompetitionGroupSetEntityMerger.merge(
                addCompetitionGroupSetRequest,
                competitionGroupSet
        );
        return competitionGroupSet;
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, org.coner.api.entity.CompetitionGroupSet> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, org.coner.hibernate.entity.CompetitionGroupSet>
    buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setCompetitionGroupSetId(source.getId());
        });
    }

    @Override
    protected EntityMerger<org.coner.hibernate.entity.CompetitionGroupSet, CompetitionGroupSet>
    buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getCompetitionGroupSetId());
        });
    }
}
