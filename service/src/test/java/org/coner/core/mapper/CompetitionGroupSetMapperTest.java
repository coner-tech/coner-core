package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.google.common.collect.Sets;

public class CompetitionGroupSetMapperTest {

    private CompetitionGroupSetMapper mapper = Mappers.getMapper(CompetitionGroupSetMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddCompetitionGroupSetRequest apiAddRequest = new AddCompetitionGroupSetRequest();
        apiAddRequest.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        apiAddRequest.setCompetitionGroupIds(Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID));
        CompetitionGroupSetAddPayload expected = new CompetitionGroupSetAddPayload();
        expected.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        expected.setCompetitionGroupIds(Sets.newHashSet(TestConstants.COMPETITION_GROUP_ID));
        expected.setCompetitionGroups(null);

        CompetitionGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequestWithNullCompetitionGroupIds() {
        AddCompetitionGroupSetRequest apiAddRequest = new AddCompetitionGroupSetRequest();
        apiAddRequest.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        apiAddRequest.setCompetitionGroupIds(null);
        CompetitionGroupSetAddPayload expected = new CompetitionGroupSetAddPayload();
        expected.setName(TestConstants.COMPETITION_GROUP_SET_NAME);
        expected.setCompetitionGroupIds(Sets.newHashSet());
        expected.setCompetitionGroups(null);

        CompetitionGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        CompetitionGroupSet competitionGroupSet = DomainEntityTestUtils.fullCompetitionGroupSet();
        CompetitionGroupSetApiEntity expected = ApiEntityTestUtils.fullCompetitionGroupSet();

        CompetitionGroupSetApiEntity actual = mapper.toApiEntity(competitionGroupSet);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<CompetitionGroupSet> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullCompetitionGroupSet());
        List<CompetitionGroupSetApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullCompetitionGroupSet());

        List<CompetitionGroupSetApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
