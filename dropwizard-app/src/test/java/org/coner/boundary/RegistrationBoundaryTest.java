package org.coner.boundary;

import org.coner.core.domain.Registration;
import org.coner.util.ApiEntityTestUtils;
import org.coner.util.DomainEntityTestUtils;
import org.coner.util.TestConstants;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 *
 */
public class RegistrationBoundaryTest {

    private final EventBoundary eventBoundary = mock(EventBoundary.class);

    private final String registrationID = TestConstants.REGISTRATION_ID;
    private final String registrationFirstName = TestConstants.REGISTRATION_FIRSTNAME;
    private final String registrationLastName = TestConstants.REGISTRATION_LASTNAME;

    private RegistrationBoundary registrationBoundary;

    @Before
    public void setup() {
        registrationBoundary = new RegistrationBoundary(eventBoundary);
    }

    @Test
    public void whenMergeApiIntoDomainItShouldMerge() {
        org.coner.api.entity.Registration apiRegistration = ApiEntityTestUtils.fullApiRegistration();
        Registration domainRegistration = new Registration();

        registrationBoundary.merge(apiRegistration, domainRegistration);

        assertThat(domainRegistration.getId()).isEqualTo(registrationID);
        assertThat(domainRegistration.getFirstName()).isEqualTo(registrationFirstName);
        assertThat(domainRegistration.getLastName()).isEqualTo(registrationLastName);
    }

    @Test
    public void whenMergeDomainIntoApiItShouldMerge() {
        Registration domainRegistration = DomainEntityTestUtils.fullDomainRegistration();
        org.coner.api.entity.Registration apiRegistration = new org.coner.api.entity.Registration();

        registrationBoundary.merge(domainRegistration, apiRegistration);

        assertThat(apiRegistration.getId()).isEqualTo(registrationID);
        assertThat(apiRegistration.getFirstName()).isEqualTo(registrationFirstName);
        assertThat(apiRegistration.getLastName()).isEqualTo(registrationLastName);
    }
}
