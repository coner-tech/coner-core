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
    public void whenToAddPayloadFromAddRegistrationRequestEventId() {
        AddRegistrationRequest addRegistrationRequest = new AddRegistrationRequest();
        addRegistrationRequest.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        addRegistrationRequest.setLastName(TestConstants.REGISTRATION_LASTNAME);
        RegistrationAddPayload expected = new RegistrationAddPayload();
        expected.setFirstName(TestConstants.REGISTRATION_FIRSTNAME);
        expected.setLastName(TestConstants.REGISTRATION_LASTNAME);
        expected.setEventId(TestConstants.EVENT_ID);

        RegistrationAddPayload actual = mapper.toAddPayload(addRegistrationRequest, TestConstants.EVENT_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntityFromEventDomainEntity() {
        Registration registration = DomainEntityTestUtils.fullDomainRegistration();
        RegistrationApiEntity expected = ApiEntityTestUtils.fullApiRegistration();

        RegistrationApiEntity actual = mapper.toApiEntity(registration);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void whenToApiEntitiesListFromDomainEntitiesList() {
        List<Registration> events = Arrays.asList(DomainEntityTestUtils.fullDomainRegistration());
        List<RegistrationApiEntity> expected = Arrays.asList(ApiEntityTestUtils.fullApiRegistration());

        List<RegistrationApiEntity> actual = mapper.toApiEntitiesList(events);

        assertThat(actual).isEqualTo(expected);
    }
}
