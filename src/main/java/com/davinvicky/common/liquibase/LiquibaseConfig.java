package com.davinvicky.common.liquibase;

import java.util.Map;

/**
 * Created by RyanDa on 02/03/2016.
 * config class for liquibase
 */
public class LiquibaseConfig {

    private LiquibaseAction actionToPerform;
    private String contexts = null;
    private String masterChangeLog;
    private Map<String, String> parameters;
    private String defaultSchema;
    private Boolean dropDBFirst;

    public String getContexts() {
        return contexts;
    }

    public String getMasterChangeLog() {
        return masterChangeLog;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Boolean isDropDBFirst() {
        return dropDBFirst;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public void setMasterChangeLog(String masterChangeLog) {
        this.masterChangeLog = masterChangeLog;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    protected void setDropDBFirst(Boolean dropDBFirst) {
        this.dropDBFirst = dropDBFirst;
    }

    public LiquibaseAction getActionToPerform() {
        return actionToPerform;
    }

    public void setActionToPerform(LiquibaseAction actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    @Override
    public String toString() {
        return "LiquibaseConfig{" +
                "actionToPerform=" + actionToPerform +
                ", contexts='" + contexts + '\'' +
                ", masterChangeLog='" + masterChangeLog + '\'' +
                ", parameters=" + parameters +
                ", defaultSchema='" + defaultSchema + '\'' +
                ", dropDBFirst=" + dropDBFirst +
                '}';
    }
}
