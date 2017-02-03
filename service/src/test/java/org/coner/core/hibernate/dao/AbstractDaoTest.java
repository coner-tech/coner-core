package org.coner.core.hibernate.dao;

import io.dropwizard.testing.junit.DAOTestRule;

abstract class AbstractDaoTest {
    DAOTestRule.Builder daoTestRuleBuilder;

    AbstractDaoTest() {
        daoTestRuleBuilder = DAOTestRule.newBuilder()
            .setDriver(org.hsqldb.jdbc.JDBCDriver.class)
            .setUrl("jdbc:hsqldb:mem:coner-" + getClass().getSimpleName());
    }

    DAOTestRule.Builder getDaoTestRuleBuilder() {
        return daoTestRuleBuilder;
    }
}
