package org.coner.it;

import org.coner.ConerDropwizardConfiguration;

import io.dropwizard.testing.junit.DropwizardAppRule;
import javax.ws.rs.client.Client;
import org.junit.*;

public class AbstractIntegrationTest {
    @ClassRule
    public static final DropwizardAppRule<ConerDropwizardConfiguration> RULE = IntegrationTestUtils.buildAppRule();

    protected static Client client;

    @BeforeClass
    public static void setupClass() {
        client = IntegrationTestUtils.buildClient(RULE);
    }

    @AfterClass
    public static void teardownClass() {
        client.close();
    }
}
