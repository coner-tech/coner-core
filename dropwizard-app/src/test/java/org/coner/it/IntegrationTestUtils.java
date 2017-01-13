package org.coner.it;

import javax.ws.rs.client.Client;

import org.coner.ConerDropwizardApplication;
import org.coner.ConerDropwizardConfiguration;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import com.google.common.base.Joiner;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public final class IntegrationTestUtils {

    private static final String TEST_CLIENT_NAME = "test-client";

    private IntegrationTestUtils() {
    }

    public static DropwizardAppRule<ConerDropwizardConfiguration> buildAppRule() {
        return new DropwizardAppRule<>(
                ConerDropwizardApplication.class,
                ResourceHelpers.resourceFilePath("config/test.yml")
        );
    }

    public static Client buildClient(DropwizardAppRule<ConerDropwizardConfiguration> appRule) {
        return buildClient(appRule, null);
    }

    public static Client buildClient(DropwizardAppRule<ConerDropwizardConfiguration> appRule,
                                     String clientNamePrefix) {
        Joiner joiner = Joiner.on("-").skipNulls();
        return new JerseyClientBuilder(appRule.getEnvironment())
                .using(appRule.getConfiguration().getJerseyClientConfiguration())
                .build(joiner.join(clientNamePrefix, TEST_CLIENT_NAME));
    }

    public static JerseyUriBuilder jerseyUriBuilderForApp(DropwizardAppRule<ConerDropwizardConfiguration> appRule) {
        return new JerseyUriBuilder()
                .scheme("http")
                .host("localhost")
                .port(appRule.getLocalPort());
    }

}
