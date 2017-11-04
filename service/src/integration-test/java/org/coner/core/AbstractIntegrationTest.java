package org.coner.core;

import javax.ws.rs.client.Client;

import org.coner.core.ConerCoreConfiguration;
import org.coner.core.util.IntegrationTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import io.dropwizard.testing.junit.DropwizardAppRule;

public class AbstractIntegrationTest {
    protected AbstractIntegrationTest() {
        // HideUtilityClassConstructor
    }

    @ClassRule
    public static final DropwizardAppRule<ConerCoreConfiguration> RULE = IntegrationTestUtils.buildAppRule();

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
