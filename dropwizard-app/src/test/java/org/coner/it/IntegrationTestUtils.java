package org.coner.it;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.coner.ConerDropwizardApplication;
import org.coner.ConerDropwizardConfiguration;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import javax.ws.rs.client.Client;

/**
 *
 */
public class IntegrationTestUtils {

    public static DropwizardAppRule<ConerDropwizardConfiguration> buildAppRule() {
        return new DropwizardAppRule<>(
                ConerDropwizardApplication.class,
                ResourceHelpers.resourceFilePath("config/test.yml")
        );
    }

    public static Client buildClient(DropwizardAppRule<ConerDropwizardConfiguration> appRule) {
        return new JerseyClientBuilder(appRule.getEnvironment())
                .using(appRule.getConfiguration().getJerseyClientConfiguration())
                .build("test client");
    }

    public static JerseyUriBuilder jerseyUriBuilderForApp(DropwizardAppRule<ConerDropwizardConfiguration> appRule) {
        return new JerseyUriBuilder()
                .scheme("http")
                .host("localhost")
                .port(appRule.getLocalPort());
    }
}
