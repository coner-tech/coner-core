package org.coner.boundary;

import org.coner.api.entity.CompetitionGroupSetApiEntity;
import org.coner.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.CompetitionGroupSet;
import org.coner.hibernate.entity.CompetitionGroupSetHibernateEntity;

public class CompetitionGroupSetBoundary extends AbstractBoundary<
        CompetitionGroupSetApiEntity,
        CompetitionGroupSet,
        CompetitionGroupSetHibernateEntity
        > {

    private EntityMerger<org.coner.api.request.AddCompetitionGroupSetRequest, CompetitionGroupSet>
            apiAddCompetitionGroupSetRequestToDomainCompetitionGroupSetEntityMerger;

    @Override
    protected EntityMerger<CompetitionGroupSetApiEntity, CompetitionGroupSet> buildApiToDomainMerger() {
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
    protected EntityMerger<CompetitionGroupSet, CompetitionGroupSetApiEntity> buildDomainToApiMerger() {
        return new ReflectionEntityMerger<>();
    }

    @Override
    protected EntityMerger<CompetitionGroupSet, CompetitionGroupSetHibernateEntity>
    buildDomainToHibernateMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setCompetitionGroupSetId(source.getId());
        });
    }

    @Override
    protected EntityMerger<CompetitionGroupSetHibernateEntity, CompetitionGroupSet>
    buildHibernateToDomainMerger() {
        return new ReflectionEntityMerger<>((source, destination) -> {
            destination.setId(source.getCompetitionGroupSetId());
        });
    }
}
