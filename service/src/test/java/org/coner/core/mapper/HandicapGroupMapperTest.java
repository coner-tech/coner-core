package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.HandicapGroupApiEntity;
import org.coner.core.api.request.AddHandicapGroupRequest;
import org.coner.core.domain.entity.HandicapGroup;
import org.coner.core.domain.payload.HandicapGroupAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class HandicapGroupMapperTest {

    private HandicapGroupMapper mapper = Mappers.getMapper(HandicapGroupMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddHandicapGroupRequest apiAddRequest = ApiRequestTestUtils.fullAddHandicapGroup();
        HandicapGroupAddPayload expected = DomainPayloadTestUtils.fullHandicapGroupAdd();

        HandicapGroupAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        HandicapGroup domainEntity = DomainEntityTestUtils.fullHandicapGroup();
        HandicapGroupApiEntity expected = ApiEntityTestUtils.fullHandicapGroup();

        HandicapGroupApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<HandicapGroup> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullHandicapGroup());
        List<HandicapGroupApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullHandicapGroup());

        List<HandicapGroupApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
