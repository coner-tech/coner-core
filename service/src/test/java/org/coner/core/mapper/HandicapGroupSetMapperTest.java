package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.hibernate.dao.HandicapGroupDao;
import org.coner.core.hibernate.dao.HandicapGroupSetDao;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

import com.google.common.collect.Sets;

public class HandicapGroupSetMapperTest {

    private HandicapGroupSetMapper mapper;

    @Mock
    HandicapGroupSetDao dao;
    @Mock
    HandicapGroupDao handicapGroupDao;

    @Before
    public void setup() {
        mapper = Mappers.getMapper(HandicapGroupSetMapper.class);
        mapper.setDao(dao);
        HandicapGroupMapper handicapGroupMapper = Mappers.getMapper(HandicapGroupMapper.class);
        handicapGroupMapper.setDao(handicapGroupDao);
        mapper.setHandicapGroupMapper(handicapGroupMapper);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddHandicapGroupSetRequest apiAddRequest = ApiRequestTestUtils.fullAddHandicapGroupSet();
        HandicapGroupSetAddPayload expected = DomainPayloadTestUtils.fullHandicapGroupSetAdd();
        expected.setHandicapGroups(null); // mapper not responsible for resolving domain entities

        HandicapGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequestWithNullHandicapGroupIds() {
        AddHandicapGroupSetRequest apiAddRequest = ApiRequestTestUtils.fullAddHandicapGroupSet();
        apiAddRequest.setHandicapGroupIds(null);
        HandicapGroupSetAddPayload expected = DomainPayloadTestUtils.fullHandicapGroupSetAdd();
        expected.setHandicapGroupIds(Sets.newHashSet());
        expected.setHandicapGroups(null);

        HandicapGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        HandicapGroupSet domainEntity = DomainEntityTestUtils.fullHandicapGroupSet();
        HandicapGroupSetApiEntity expected = ApiEntityTestUtils.fullHandicapGroupSet();

        HandicapGroupSetApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<HandicapGroupSet> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullHandicapGroupSet());
        List<HandicapGroupSetApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullHandicapGroupSet());

        List<HandicapGroupSetApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
