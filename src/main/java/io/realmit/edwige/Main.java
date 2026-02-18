package io.realmit.edwige;

import io.realmit.edwige.api.http.ApiServer;
import io.realmit.edwige.api.listener.PendingItemJoinListener;
import io.realmit.edwige.api.service.PendingItemStoreService;
import io.realmit.edwige.command.RedeemCommand;
import io.realmit.edwige.services.MessageService;
import io.realmit.edwige.services.PlayerActionsService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private MessageService messageService;
    private PendingItemStoreService pendingItemStoreService;
    private PlayerActionsService playerActionsService;
    private PendingItemJoinListener pendingItemJoinListener;

    @Override
    public void onEnable() {
        initAll();
    }

    private void initAll() {
        initServices();
        initApi();
        initListeners();
        registerEvents();
        registerCommands();
    }

    private void initServices() {
        if (messageService == null) {
            messageService = new MessageService(this);
        } else {
            messageService.reload();
        }

        pendingItemStoreService = new PendingItemStoreService(this);
        playerActionsService = new PlayerActionsService(pendingItemStoreService);
    }

    private void initApi() {
        ApiServer apiServer = new ApiServer(messageService, pendingItemStoreService, this, 8081);

        try {
            apiServer.start();
        } catch (Exception e) {
            getLogger().severe("[initApi] Failed to start API server: " + e.getMessage());
        }
    }

    private void initListeners() {
        pendingItemJoinListener = new PendingItemJoinListener(
                messageService,
                pendingItemStoreService,
                playerActionsService
        );
    }

    private void registerEvents() {
        if (null != pendingItemStoreService) {
            Bukkit.getPluginManager().registerEvents(pendingItemJoinListener, this);
        }
    }

    private void registerCommands() {
        registerCommand(
                "redeem",
                new RedeemCommand(messageService, playerActionsService, pendingItemStoreService)
        );
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand cmd = checkCommand(commandName);
        cmd.setExecutor(executor);
    }

    private PluginCommand checkCommand(String cmdName) {
        PluginCommand cmd = getCommand(cmdName);
        if (null == cmd) {
            getLogger().severe(
                    "[checkCommand] Command 'edwige:" + cmdName + "' not found in plugin.yml, disabling plugin."
            );
        }

        return cmd;
    }
}
