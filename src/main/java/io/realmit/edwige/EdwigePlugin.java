package io.realmit.edwige;

import io.realmit.edwige.api.shared.http.ApiServer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class EdwigePlugin extends JavaPlugin {

    private static EdwigePlugin plugin;
    private PluginContext context;
    private ApiServer apiServer;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        context = new PluginContext();

        initApi();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        if (apiServer != null) {
            apiServer.stop();
        }
    }

    private int getApiPort() {
        int port = getConfig().getInt("modules.api.config.port", 8080);

        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid API port in config: " + port);
        }

        return port;
    }

    private String getApiToken() {
        String token = getConfig().getString("modules.api.security.bearerToken");

        if (token == null || token.isEmpty()) {
            getLogger().warning("[API] No bearer token configured; API will be unsecured.");
        }

        return token;
    }

    private void initApi() {
        apiServer = new ApiServer(
                context.getChatQuestionService(),
                context.getMessageService(),
                this,
                getApiPort(),
                getApiToken()
        );

        try {
            apiServer.start();
        } catch (Exception e) {
            getLogger().severe("[API] Failed to start API server: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            throw new IllegalStateException("Failed to start API server", e);
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(context.getChatQuestionListener(), this);
        getServer().getPluginManager().registerEvents(context.getMenuListener(), this);
        getServer().getPluginManager().registerEvents(context.getMailboxListener(), this);
        getServer().getPluginManager().registerEvents(context.getPlayerJoinListener(), this);
    }

    private void registerCommands() {
        registerCommand("mailbox", context.getMailBoxCommandExecutor());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand cmd = getCommand(name);
        if (cmd == null) {
            String message = "[Command] 'edwige:" + name + "' not found in plugin.yml, disabling plugin.";
            getLogger().severe(message);
            getServer().getPluginManager().disablePlugin(this);
            throw new IllegalStateException(message);
        }
        cmd.setExecutor(executor);
    }

    public PluginContext getContext() {
        return context;
    }

    public static EdwigePlugin getPlugin() {
        return plugin;
    }
}
