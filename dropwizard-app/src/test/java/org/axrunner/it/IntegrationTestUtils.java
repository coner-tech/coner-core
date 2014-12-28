package org.axrunner.it;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.axrunner.AxRunnerDropwizardApplication;
import org.axrunner.AxRunnerDropwizardConfiguration;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import javax.ws.rs.client.Client;

/**
 *
 */
public class IntegrationTestUtils {

    public static DropwizardAppRule<AxRunnerDropwizardConfiguration> buildAppRule() {
        return new DropwizardAppRule<>(
                AxRunnerDropwizardApplication.class,
                ResourceHelpers.resourceFilePath("config/test.yml")
        );
    }

    public static Client buildClient(DropwizardAppRule<AxRunnerDropwizardConfiguration> appRule) {
        return new JerseyClientBuilder(appRule.getEnvironment())
                .using(appRule.getConfiguration().getJerseyClientConfiguration())
                .build("test client");
    }

    public static JerseyUriBuilder jerseyUriBuilderForApp(DropwizardAppRule<AxRunnerDropwizardConfiguration> appRule) {
        return new JerseyUriBuilder()
                .scheme("http")
                .host("localhost")
                .port(appRule.getLocalPort());
    }
}
