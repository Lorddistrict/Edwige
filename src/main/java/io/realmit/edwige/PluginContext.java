package io.realmit.edwige;

import io.realmit.edwige.api.listeners.ChatQuestionListener;
import io.realmit.edwige.commands.MailBoxCommand;
import io.realmit.edwige.config.command.CommandConfig;
import io.realmit.edwige.config.mailbox.MailBoxMenuConfig;
import io.realmit.edwige.listeners.MailBoxListener;
import io.realmit.edwige.listeners.MenuListener;
import io.realmit.edwige.menu.utils.MenuUtils;
import io.realmit.edwige.services.ChatQuestionService;
import io.realmit.edwige.services.MessageService;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public final class PluginContext {

    private final Map<UUID, String> openMenus = new HashMap<>();
    private static final HashMap<Player, MenuUtils> menuUtilsMap = new HashMap<>();

    private final MailBoxMenuConfig mailboxesConfig;
    private final MessageService messageService;
    private final ChatQuestionService chatQuestionService;
    private final ChatQuestionListener chatQuestionListener;
    private final MailBoxListener mailboxListener;
    private final MenuListener menuListener;
    private final CommandConfig commandConfig;

    public PluginContext() {
        this.messageService = new MessageService();
        this.chatQuestionService = new ChatQuestionService(messageService);
        this.chatQuestionListener = new ChatQuestionListener(chatQuestionService);
        this.mailboxesConfig = new MailBoxMenuConfig();
        this.mailboxListener = new MailBoxListener();
        this.menuListener = new MenuListener();
        this.commandConfig = new CommandConfig();
    }

    public void registerOpenMenu(UUID playerId, String menuType) {
        openMenus.put(playerId, menuType);
    }

    public void unregisterOpenMenu(UUID playerId) {
        openMenus.remove(playerId);
    }

    public boolean hasMenuOpen(UUID playerId, String menuType) {
        return menuType.equals(openMenus.get(playerId));
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

    public Listener getChatQuestionListener() {
        return chatQuestionListener;
    }

    public Listener getMailboxListener() {
        return mailboxListener;
    }

    public CommandExecutor getMailBoxCommandExecutor() {
        return new MailBoxCommand();
    }

    public Listener getMenuListener() {
        return menuListener;
    }

    public CommandConfig getCommandConfig() {
        return commandConfig;
    }

    public static MenuUtils getPlayerMenuUtils(Player player) {
        MenuUtils menuUtils;

        if (!menuUtilsMap.containsKey(player)) {
            menuUtils = new MenuUtils(player);
            menuUtilsMap.put(player, menuUtils);

            return menuUtils;
        }

        return menuUtilsMap.get(player);
    }
}
