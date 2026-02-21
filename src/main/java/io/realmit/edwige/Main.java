package io.realmit.edwige;

import io.realmit.edwige.api.http.ApiServer;
import io.realmit.edwige.api.listeners.ChatQuestionListener;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private ChatQuestionService chatQuestionService;
    private MessageService messageService;

    private ChatQuestionListener chatQuestionListener;

    @Override
    public void onEnable() {
        initAll();
    }

    private void initAll() {
        initConfig();
        initServices();
        initApi();
        initListeners();
        registerEvents();
        registerCommands();
    }

    private void initConfig() {
        saveDefaultConfig();
    }

    private void initServices() {
        messageService = new MessageService(this);
        chatQuestionService = new ChatQuestionService(messageService, this);
    }

    private void initApi() {
        ApiServer apiServer = new ApiServer(
                chatQuestionService,
                messageService,
                this,
                getConfig().getInt("modules.api.config.port"),
                getConfig().getString("modules.api.security.bearerToken")
        );

        try {
            apiServer.start();
        } catch (Exception e) {
            getLogger().severe("[initApi] Failed to start API server: " + e.getMessage());
        }
    }

    private void initListeners() {
        chatQuestionListener = new ChatQuestionListener(chatQuestionService);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(chatQuestionListener, this);
    }

    private void registerCommands() {
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand cmd = checkCommand(commandName);
        cmd.setExecutor(executor);
    }

    private PluginCommand checkCommand(String cmdName) {
        PluginCommand cmd = getCommand(cmdName);
        if (cmd == null) {
            String message = "[Main] Command 'edwige:" + cmdName + "' not found in plugin.yml, disabling plugin.";
            getLogger().severe(message);
        }

        return cmd;
    }
}
