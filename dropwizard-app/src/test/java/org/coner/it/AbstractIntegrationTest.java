package org.coner.it;

import io.dropwizard.testing.junit.DropwizardAppRule;
import org.coner.ConerDropwizardConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import javax.ws.rs.client.Client;

/**
 *
 */
public class AbstractIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ConerDropwizardConfiguration> RULE = IntegrationTestUtils.buildAppRule();

    protected static Client client;

    @BeforeClass
    public static void setupClass() {
        client = IntegrationTestUtils.buildClient(RULE, CreateEventIntegrationTest.class.getSimpleName());
    }

    @AfterClass
    public static void teardownClass() {
        client.close();
    }
}
