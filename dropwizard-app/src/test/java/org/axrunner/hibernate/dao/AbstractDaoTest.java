package org.axrunner.hibernate.dao;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import org.axrunner.hibernate.entity.Event;
import org.axrunner.hibernate.entity.HibernateEntity;
import org.axrunner.hibernate.entity.Registration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class AbstractDaoTest {

    private final Class<? extends HibernateEntity>[] hibernateEntityClasses = new Class[]{
            Event.class,
            Registration.class,
    };

    private SessionFactory sessionFactory;

    public AbstractDaoTest() {
        Configuration configuration = new Configuration();
        for (Class<? extends HibernateEntity> hibernateEntityClass : hibernateEntityClasses) {
            configuration.addAnnotatedClass(hibernateEntityClass);
        }
        configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.HSQLDialect");
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "create");
        configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        configuration.setProperty(AvailableSettings.USE_GET_GENERATED_KEYS, "true");
        configuration.setProperty(AvailableSettings.GENERATE_STATISTICS, "true");
        configuration.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, "true");
        configuration.setProperty(AvailableSettings.ORDER_UPDATES, "true");
        configuration.setProperty(AvailableSettings.ORDER_INSERTS, "true");
        configuration.setProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true");
        configuration.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        ConnectionProvider connectionProvider = buildConnectionProvider();
        try {
            Connection connection = connectionProvider.getConnection();
            if (connection.isClosed()) {
                throw new RuntimeException("Connection closed, wtf");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .addService(ConnectionProvider.class, connectionProvider)
                .build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    private ConnectionProvider buildConnectionProvider() {
        DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
        dataSourceFactory.setUser("sa");
        dataSourceFactory.setUrl("jdbc:hsqldb:mem:axrunner-" + getClass().getSimpleName());
        dataSourceFactory.setValidationQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES");
        MetricRegistry metricRegistry = new MetricRegistry();
        try {
            connectionProvider.setDataSource(
                    dataSourceFactory.build(metricRegistry, "axrunner-test")
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> configValues = new HashMap<>();
        configValues.put(Environment.USER, "sa");
        connectionProvider.configure(configValues);
        return connectionProvider;
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
