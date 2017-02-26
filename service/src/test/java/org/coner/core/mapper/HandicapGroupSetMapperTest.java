package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.HandicapGroupSetApiEntity;
import org.coner.core.api.request.AddHandicapGroupSetRequest;
import org.coner.core.domain.entity.HandicapGroupSet;
import org.coner.core.domain.payload.HandicapGroupSetAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.google.common.collect.Sets;

public class HandicapGroupSetMapperTest {

    private HandicapGroupSetMapper mapper = Mappers.getMapper(HandicapGroupSetMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddHandicapGroupSetRequest apiAddRequest = ApiRequestTestUtils.fullAddHandicapGroupSetRequest();
        HandicapGroupSetAddPayload expected = new HandicapGroupSetAddPayload();
        expected.setName(TestConstants.HANDICAP_GROUP_SET_NAME);
        expected.setHandicapGroupIds(Sets.newHashSet(TestConstants.HANDICAP_GROUP_ID));
        expected.setHandicapGroups(null);

        HandicapGroupSetAddPayload actual = mapper.toDomainAddPayload(apiAddRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToDomainAddPayloadFromApiAddRequestWithNullHandicapGroupIds() {
        AddHandicapGroupSetRequest apiAddRequest = new AddHandicapGroupSetRequest();
        apiAddRequest.setName(TestConstants.HANDICAP_GROUP_SET_NAME);
        apiAddRequest.setHandicapGroupIds(null);
        HandicapGroupSetAddPayload expected = new HandicapGroupSetAddPayload();
        expected.setName(TestConstants.HANDICAP_GROUP_SET_NAME);
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
