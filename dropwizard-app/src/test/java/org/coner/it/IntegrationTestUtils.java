package org.coner.it;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.coner.ConerDropwizardApplication;
import org.coner.ConerDropwizardConfiguration;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import javax.ws.rs.client.Client;

/**
 *
 */
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
        // This should be removed when https://github.com/dropwizard/dropwizard/issues/832 is fixed
        stripLingeringMetrics(appRule.getEnvironment());
        return new JerseyClientBuilder(appRule.getEnvironment())
                .using(appRule.getConfiguration().getJerseyClientConfiguration())
                .build(TEST_CLIENT_NAME);
    }

    public static JerseyUriBuilder jerseyUriBuilderForApp(DropwizardAppRule<ConerDropwizardConfiguration> appRule) {
        return new JerseyUriBuilder()
                .scheme("http")
                .host("localhost")
                .port(appRule.getLocalPort());
    }

    /**
     * This is to remove the metrics that the constructor of InstrumentedHttpClientConnectionManager
     * adds to an environments MetricsRegistry (related to https://github.com/dropwizard/dropwizard/issues/832).
     *
     * @param env Environment whose MetricRegistry should be stripped
     */
    private static void stripLingeringMetrics(Environment env) {
        env.metrics().removeMatching(new MetricFilter() {
            @Override
            public boolean matches(String name, Metric metric) {
                return name.contains(TEST_CLIENT_NAME);
            }
        });
    }
}
