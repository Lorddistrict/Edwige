package io.realmit.edwige.config.command;

import io.realmit.edwige.config.Config;

public final class CommandConfig extends Config {

    public static final String CONFIG_FILE_NAME = "commands.yml";

    public CommandConfig() {
        super();
    }

    @Override
    public String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }
}
