package io.realmit.edwige;

import io.realmit.edwige.api.listeners.ChatQuestionListener;
import io.realmit.edwige.commands.MailBoxCommand;
import io.realmit.edwige.config.MailBoxMenuConfig;
import io.realmit.edwige.listeners.MailBoxListener;
import io.realmit.edwige.menu.MailBoxMenu2;
import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginContext {

    private final MailBoxMenuConfig mailboxesConfig;
    private final MessageService messageService;
    private final ChatQuestionService chatQuestionService;
    private final PlayerMenuUtils playerMenuUtils;
    private final MailBoxMenu2 mailBoxMenu2;
    private final ChatQuestionListener chatQuestionListener;
    private final MailBoxListener mailboxListener;

    public PluginContext(JavaPlugin plugin) {
        this.mailboxesConfig = new MailBoxMenuConfig(plugin);
        this.messageService = new MessageService(plugin);
        this.chatQuestionService = new ChatQuestionService(messageService, plugin);
        this.playerMenuUtils = new PlayerMenuUtils();
        this.mailBoxMenu2 = new MailBoxMenu2(playerMenuUtils);
        this.chatQuestionListener = new ChatQuestionListener(chatQuestionService);
        this.mailboxListener = new MailBoxListener();
    }

    public MailBoxMenuConfig getMailboxesConfig() {
        return mailboxesConfig;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public ChatQuestionService getChatQuestionService() {
        return chatQuestionService;
    }

    public PlayerMenuUtils getPlayerMenuUtils() {
        return playerMenuUtils;
    }

//    public MailBoxMenu getMailBoxMenu() {
//        return mailBoxMenu;
//    }

    public MailBoxMenu2 getMailBoxMenu2() {
        return mailBoxMenu2;
    }

    public Listener getChatQuestionListener() {
        return chatQuestionListener;
    }

    public Listener getMailboxListener() {
        return mailboxListener;
    }

    public CommandExecutor getMailBoxCommandExecutor() {
        return new MailBoxCommand(mailBoxMenu2);
    }
}
