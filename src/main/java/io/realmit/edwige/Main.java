package io.realmit.edwige;

import io.realmit.edwige.api.http.ApiServer;
import io.realmit.edwige.api.listeners.ChatQuestionListener;
import io.realmit.edwige.commands.MailBoxCommand;
import io.realmit.edwige.config.MailBoxMenuConfig;
import io.realmit.edwige.listeners.MailBoxListener;
import io.realmit.edwige.menu.MailBoxMenu;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private MailBoxMenuConfig mailboxesConfig;

    private ChatQuestionListener chatQuestionListener;
    private MailBoxListener mailboxListener;

    private MailBoxMenu mailBoxMenu;

    private ChatQuestionService chatQuestionService;
    private MessageService messageService;

    @Override
    public void onEnable() {
        initAll();
    }

    private void initAll() {
        initConfig();
        initServices();
        initApi();
        initMenus();
        initListeners();
        registerEvents();
        registerCommands();
    }

    private void initConfig() {
        saveDefaultConfig();
        mailboxesConfig = new MailBoxMenuConfig(this);
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
            getLogger().severe("[API] Failed to start API server: " + e.getMessage());
        }
    }

    private void initMenus() {
        mailBoxMenu = new MailBoxMenu(this, mailboxesConfig);
    }

    private void initListeners() {
        chatQuestionListener = new ChatQuestionListener(chatQuestionService);
        mailboxListener = new MailBoxListener(mailBoxMenu);
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(chatQuestionListener, this);
        getServer().getPluginManager().registerEvents(mailboxListener, this);
    }

    private void registerCommands() {
        registerCommand("mailbox", new MailBoxCommand(mailBoxMenu));
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand cmd = checkCommand(commandName);
        cmd.setExecutor(executor);
    }

    private PluginCommand checkCommand(String cmdName) {
        PluginCommand cmd = getCommand(cmdName);
        if (cmd == null) {
            String message = "[Command] 'edwige:" + cmdName + "' not found in plugin.yml, disabling plugin.";
            getLogger().severe(message);
        }

        return cmd;
    }
}
