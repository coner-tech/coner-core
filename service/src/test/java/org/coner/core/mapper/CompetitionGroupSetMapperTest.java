package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.CompetitionGroupSetApiEntity;
import org.coner.core.api.request.AddCompetitionGroupSetRequest;
import org.coner.core.domain.entity.CompetitionGroupSet;
import org.coner.core.domain.payload.CompetitionGroupSetAddPayload;
import org.coner.core.hibernate.dao.CompetitionGroupDao;
import org.coner.core.hibernate.dao.CompetitionGroupSetDao;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionGroupSetMapperTest {

    private CompetitionGroupSetMapper mapper;

    @Mock
    CompetitionGroupSetDao dao;
    @Mock
    CompetitionGroupDao competitionGroupDao;

    @Before
    public void setup() {
         mapper = Mappers.getMapper(CompetitionGroupSetMapper.class);
         mapper.setDao(dao);
         CompetitionGroupMapper competitionGroupMapper = Mappers.getMapper(CompetitionGroupMapper.class);
         competitionGroupMapper.setDao(competitionGroupDao);
         mapper.setCompetitionGroupMapper(competitionGroupMapper);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddCompetitionGroupSetRequest apiAddRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet();
        CompetitionGroupSetAddPayload expected = DomainPayloadTestUtils.fullCompetitionGroupSetAdd();
        expected.setCompetitionGroups(null);

        CompetitionGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequestWithNullCompetitionGroupIds() {
        AddCompetitionGroupSetRequest apiAddRequest = ApiRequestTestUtils.fullAddCompetitionGroupSet();
        apiAddRequest.setCompetitionGroupIds(null);
        CompetitionGroupSetAddPayload expected = DomainPayloadTestUtils.fullCompetitionGroupSetAdd();
        expected.setCompetitionGroupIds(Sets.newHashSet());
        expected.setCompetitionGroups(null);

        CompetitionGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        CompetitionGroupSet domainEntity = DomainEntityTestUtils.fullCompetitionGroupSet();
        CompetitionGroupSetApiEntity expected = ApiEntityTestUtils.fullCompetitionGroupSet();

        CompetitionGroupSetApiEntity actual = mapper.toApiEntity(domainEntity);

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
