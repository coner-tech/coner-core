package org.axrunner.hibernate.gateway;

import io.dropwizard.hibernate.HibernateBundle;
import org.axrunner.AxRunnerDropwizardConfiguration;

/**
 *
 */
public abstract class HibernateGateway {

    private final HibernateBundle<AxRunnerDropwizardConfiguration> hibernate;

    public HibernateGateway(HibernateBundle<AxRunnerDropwizardConfiguration> hibernate) {
        this.hibernate = hibernate;
    }

    protected HibernateBundle<AxRunnerDropwizardConfiguration> getHibernate() {
        return hibernate;
    }
}
