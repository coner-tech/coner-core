package org.axrunner.hibernate.gateway;

import io.dropwizard.hibernate.HibernateBundle;
import org.axrunner.AxRunnerDropwizardConfiguration;

/**
 *
 */
public class RegistrationGateway extends HibernateGateway {

    public RegistrationGateway(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate) {
        super(hibernate);
    }

}
