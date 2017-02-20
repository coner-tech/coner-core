package org.coner.core.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.coner.core.api.entity.RegistrationApiEntity;
import org.coner.core.api.request.AddRegistrationRequest;
import org.coner.core.domain.entity.Registration;
import org.coner.core.domain.payload.RegistrationAddPayload;
import org.coner.core.util.ApiEntityTestUtils;
import org.coner.core.util.DomainEntityTestUtils;
import org.coner.core.util.TestConstants;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

public class RegistrationMapperTest {

    private RegistrationMapper mapper = Mappers.getMapper(RegistrationMapper.class);

    @Test
    public void whenToDomainAddPayloadFromApiAddRequest() {
        AddRegistrationRequest apiAddRequest = new AddRegistrationRequest();
        apiAddRequest.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        apiAddRequest.setLastName(TestConstants.REGISTRATION_LASTNAME);
        RegistrationAddPayload expected = new RegistrationAddPayload();
        expected.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        expected.setLastName(TestConstants.REGISTRATION_LASTNAME);
        expected.setEventId(TestConstants.EVENT_ID);

        RegistrationAddPayload actual = mapper.toDomainAddPayload(apiAddRequest, TestConstants.EVENT_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromDomainEntity() {
        Registration domainEntity = DomainEntityTestUtils.fullDomainRegistration();
        RegistrationApiEntity expected = ApiEntityTestUtils.fullApiRegistration();

        RegistrationApiEntity actual = mapper.toApiEntity(domainEntity);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityListFromDomainEntityList() {
        List<Registration> domainEntityList = Arrays.asList(DomainEntityTestUtils.fullDomainRegistration());
        List<RegistrationApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullApiRegistration());

        List<RegistrationApiEntity> actual = mapper.toApiEntityList(domainEntityList);

        assertThat(actual).isEqualTo(expected);
    }
}
