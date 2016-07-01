//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.davinvicky.common.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.util.StringUtils;
import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;

public class LiquibaseRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(LiquibaseRunner.class.getName());

    private LiquibaseConfig config;

    private DataSource dataSource;

    private Properties properties;

    public LiquibaseRunner(Properties properties) throws SQLException {
        this.properties = properties;
        setDataSource(createDatasource());
        setConfig(createConfig());
    }

    protected LiquibaseConfig createConfig() {
        LiquibaseConfig config = new LiquibaseConfig();

        config.setContexts(properties.getProperty("context"));
        LOGGER.info("property: context set to =\t\t\t\t" + config.getContexts());

        config.setMasterChangeLog(properties.getProperty("owner.master.changelog"));
        LOGGER.info("property: owner.master.changelog=\t\t\t\t" + config.getMasterChangeLog());

        if ("false".equals(properties.getProperty("owner.dropdb.first"))) {
            config.setDropDBFirst(false);
        } else if ("true".equals(properties.getProperty("owner.dropdb.first"))) {
            config.setDropDBFirst(true);
        }
        LOGGER.info("property: owner.dropdb.first=\t\t\t\t" + config.isDropDBFirst());

        config.setActionToPerform(LiquibaseAction.getEnum(properties.getProperty("action")));
        LOGGER.info("property: action=\t\t\t\t" + config.getActionToPerform());

        return config;
    }

    protected DataSource createDatasource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(properties.getProperty("url"));
        LOGGER.info("property: url" + "=\t\t\t\t" + dataSource.getURL());
        dataSource.setUser(properties.getProperty("owner.username"));
        dataSource.setPassword(properties.getProperty("owner.password"));
        return dataSource;
    }

    /**
     * Run liquibase with command. Default command is update
     *
     * @throws LiquibaseException
     */
    public void run() throws LiquibaseException {
        Connection connection = null;
        Liquibase liquibase;

        try {
            connection = this.dataSource.getConnection();
            liquibase = createLiquibase(connection);
            if (config.getActionToPerform() == LiquibaseAction.UPDATE_SQL) {
                Writer writer = new PrintWriter(System.out);
                liquibase.update(config.getContexts(), writer);
            } else if (config.getActionToPerform() == LiquibaseAction.FUTURE_ROLLBACK) {
                liquibase.rollback(1, config.getContexts());
            } else if (config.getActionToPerform() == LiquibaseAction.FUTURE_ROLLBACK_SQL) {
                Writer writer = new PrintWriter(System.out);
                liquibase.futureRollbackSQL(config.getContexts(), writer);
            } else if (config.getActionToPerform() == LiquibaseAction.UPDATE_TESTING_ROLLBACK) {
                liquibase.updateTestingRollback(config.getContexts());
            } else {
                liquibase.update(config.getContexts());
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex);
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException ex) {
                    // Give up
                    LOGGER.error("Failed to close Database Connection", ex);
                }
            }
        }
    }

    private Liquibase createLiquibase(Connection c) throws LiquibaseException {
        Liquibase liquibase = new Liquibase(config.getMasterChangeLog(), createResourceOpener(), createDatabase(c));
        if (config.getParameters() != null) {
            for (Entry<String, String> entry : config.getParameters().entrySet()) {
                liquibase.setChangeLogParameter(entry.getKey(), entry.getValue());
            }
        }

        if (config.isDropDBFirst()) {
            liquibase.dropAll();
        }

        return liquibase;
    }

    private Database createDatabase(Connection c) throws DatabaseException {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
        if (StringUtils.trimToNull(config.getDefaultSchema()) != null) {
            database.setDefaultSchemaName(config.getDefaultSchema());
        }
        return database;
    }

    private void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void setConfig(LiquibaseConfig config) {
        this.config = config;
    }

    private ResourceAccessor createResourceOpener() {
        return new ClassLoaderResourceAccessor();
    }

    protected Properties getProperties() {
        return properties;
    }
}
