package org.coner.core.it;

import javax.ws.rs.client.Client;

import org.coner.core.ConerCoreApplication;
import org.coner.core.ConerCoreConfiguration;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import com.google.common.base.Joiner;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;

public final class IntegrationTestUtils {

    private static final String TEST_CLIENT_NAME = "test-client";

    private IntegrationTestUtils() {
    }

    public static DropwizardAppRule<ConerCoreConfiguration> buildAppRule() {
        return new DropwizardAppRule<>(
                ConerCoreApplication.class,
                ResourceHelpers.resourceFilePath("config/test.yml")
        );
    }

    public static Client buildClient(DropwizardAppRule<ConerCoreConfiguration> appRule) {
        return buildClient(appRule, null);
    }

    public static Client buildClient(DropwizardAppRule<ConerCoreConfiguration> appRule,
                                     String clientNamePrefix) {
        Joiner joiner = Joiner.on("-").skipNulls();
        return new JerseyClientBuilder(appRule.getEnvironment())
                .using(appRule.getConfiguration().getJerseyClientConfiguration())
                .build(joiner.join(clientNamePrefix, TEST_CLIENT_NAME));
    }

    public static JerseyUriBuilder jerseyUriBuilderForApp(DropwizardAppRule<ConerCoreConfiguration> appRule) {
        return new JerseyUriBuilder()
                .scheme("http")
                .host("localhost")
                .port(appRule.getLocalPort());
    }

}
