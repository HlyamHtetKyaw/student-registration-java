package org.tutgi.student_registration.config.command;

public class CommandWrapperBuilder {

    private String entityName;
    private String actionName;
    private String json;

    public CommandWrapperBuilder withJson(final String json) {
        this.json = json;
        return this;
    }

    public CommandWrapperBuilder login() {
        this.entityName = "AUTH";
        this.actionName = "LOGIN";
        return this;
    }

    public CommandWrapper build() {
        return new CommandWrapper(entityName, actionName, json);
    }
}
