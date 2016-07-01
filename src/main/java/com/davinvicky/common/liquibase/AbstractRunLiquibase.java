package com.davinvicky.common.liquibase;

import liquibase.exception.LiquibaseException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by RyanDa on 02/03/2016.
 * Main class for running liquibase
 */
public abstract class AbstractRunLiquibase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRunLiquibase.class);
    private static HelpFormatter formatter = new HelpFormatter();
    private Properties properties;

    AbstractRunLiquibase(String[] args) {
        // 1.) Get default props
        Properties defaultProperties = getDefaultProperties();

        // 2.) Get userOption definition from default properties
        Options optionsDefinition = buildCommandlineOptionsBasedOnDefaults(defaultProperties);

        // 3.) Build commandline handler
        CommandLine cmd = createCommandLine(args, optionsDefinition);

        // 4.) Update properties
        if (cmd != null) {
            this.properties = buildFinalProperties(cmd.getOptions(), defaultProperties);
        }
    }

    private CommandLine createCommandLine(String[] args, Options optionsDefinition) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(optionsDefinition, args);
        } catch (ParseException e) {
            formatter.setWidth(120);
            formatter.printHelp("Defaults are already provided to run against localhost. If you would like to run this " +
                    "application with different properties then use any of the following commandline arguments: ", optionsDefinition);
        }
        return cmd;
    }

    private Properties getDefaultProperties() {
        Properties defaultProperties = new Properties();
        try {
            defaultProperties.load(AbstractRunLiquibase.class.getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            LOGGER.error("error loading default properties file" + e);
        }
        return defaultProperties;
    }

    private Properties buildFinalProperties(Option[] userOptions, Properties defaultProperties) {
        Properties finalProperties = new Properties(defaultProperties);

        for (Option userOption : userOptions) {
            finalProperties.put(userOption.getLongOpt(), userOption.getValue());
        }
        return finalProperties;
    }

    /**
     * Will build list of valid options based on jdbc.properties
     *
     * @return options
     */
    private Options buildCommandlineOptionsBasedOnDefaults(Properties defaultProperties) {
        Options options = new Options();
        Set<Object> keyset = defaultProperties.keySet();
        for (Object key : keyset) {
            String keyStr = key.toString();
            if (!keyStr.contains(".description")) {
                options.addOption(null, keyStr, true, defaultProperties.getProperty(keyStr + ".description"));
            }
        }
        return options;
    }

    /**
     * Places your runners here.
     *
     * @throws LiquibaseException
     */
    public abstract void runLiquibase() throws LiquibaseException;

    public Properties getProperties() {
        return properties;
    }
}
