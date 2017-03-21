package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.ApiRequestTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.DomainPayloadTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class RegistrationMapperTest {

    private RegistrationMapper mapper = Mappers.getMapper(RegistrationMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddRegistrationRequest apiAddRequest = ApiRequestTestUtils.fullAddRegistration();
        RegistrationAddPayload expected = DomainPayloadTestUtils.fullRegistrationAdd();
        expected.setEvent(null); // not mapper's job to resolve entities

        RegistrationAddPayload actual = mapper.toDomainAddPayload(apiAddRequest, TestConstants.EVENT_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        Registration domainEntity = DomainEntityTestUtils.fullRegistration();
        RegistrationApiEntity expected = ApiEntityTestUtils.fullRegistration();

        RegistrationApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<Registration> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullRegistration());
        List<RegistrationApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullRegistration());

        List<RegistrationApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
