package com.davinvicky.common.liquibase;

/**
 * Created by ryanda on 27/05/2016.
 */
enum LiquibaseAction {
    UPDATE("update"),
    UPDATE_SQL("updatesql"),
    FUTURE_ROLLBACK("futurerollback"),
    FUTURE_ROLLBACK_SQL("futurerollbacksql"),
    UPDATE_TESTING_ROLLBACK("updatetestingrollback");

    private final String commandLineAction;

    LiquibaseAction(String commandlineAction) {
        this.commandLineAction = commandlineAction;
    }

    public String getCommandLineAction() {
        return commandLineAction;
    }

    @Override
    public String toString() {
        return this.getCommandLineAction();
    }

    public static LiquibaseAction getEnum(String value) {
        for (LiquibaseAction v : values()) {
            if (v.getCommandLineAction().equalsIgnoreCase(value)) {
                return v;
            }
        }
        return null;
    }
}