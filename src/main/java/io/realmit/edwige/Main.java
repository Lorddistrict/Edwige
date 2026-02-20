package io.realmit.edwige;

import io.realmit.edwige.api.http.ApiServer;
import io.realmit.edwige.api.listeners.ChatQuestionListener;
import io.realmit.edwige.api.listeners.PendingItemJoinListener;
import io.realmit.edwige.api.services.PendingItemStoreService;
import io.realmit.edwige.commands.RedeemCommand;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import io.realmit.edwige.services.PlayerActionsService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private ChatQuestionService chatQuestionService;
    private MessageService messageService;
    private PendingItemStoreService pendingItemStoreService;
    private PlayerActionsService playerActionsService;

    private ChatQuestionListener chatQuestionListener;
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
        messageService = new MessageService(this);
        pendingItemStoreService = new PendingItemStoreService(this);
        playerActionsService = new PlayerActionsService(pendingItemStoreService);
        chatQuestionService = new ChatQuestionService(messageService, this);
    }

    private void initApi() {
        ApiServer apiServer = new ApiServer(
                chatQuestionService,
                messageService,
                this,
                8082
        );

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

        chatQuestionListener = new ChatQuestionListener(chatQuestionService);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(pendingItemJoinListener, this);
        Bukkit.getPluginManager().registerEvents(chatQuestionListener, this);
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
        if (cmd == null) {
            getLogger().severe(
                    "[checkCommand] Command 'edwige:" + cmdName + "' not found in plugin.yml, disabling plugin."
            );
        }

        return cmd;
    }

    public ChatQuestionService getConversationService() {
        return chatQuestionService;
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
