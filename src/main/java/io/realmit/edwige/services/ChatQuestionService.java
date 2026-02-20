package io.realmit.edwige.services;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class ChatQuestionService {

    private final MessageService msgService;
    private final Plugin plugin;
    private final Map<UUID, Consumer<String>> waiting = new ConcurrentHashMap<>();
    private final Map<UUID, Consumer<Boolean>> yesNoWaiting = new ConcurrentHashMap<>();

    public ChatQuestionService(
            MessageService msgService,
            Plugin plugin
    ) {
        this.msgService = msgService;
        this.plugin = plugin;
    }

    public void ask(Player player, String questionKey, Consumer<String> callback) {
        waiting.put(player.getUniqueId(), callback);
        msgService.send(player, questionKey, true);
    }

    public void handlePlayerChat(Player player, String message) {
        Consumer<String> callback = waiting.remove(player.getUniqueId());

        if (callback == null) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(message));
    }

    public void askYesNo(
            Player player,
            boolean separator,
            String titleKey,
            boolean clearChat,
            Component prefix,
            Component suffix,
            Consumer<Boolean> callback
    ) {
        UUID playerUniqueId = player.getUniqueId();
        yesNoWaiting.put(playerUniqueId, callback);

        if (clearChat) {
            msgService.clearPlayerChat(player);
        }

        if (separator) {
            msgService.send(player, "global.separator", false);
        }

        if (!titleKey.isBlank()) {
            msgService.send(player, titleKey, true);
        }

        if (separator) {
            msgService.send(player, "global.separator", false);
            msgService.send(player, "global.backlines.x1", false);
        }

        if (!Component.empty().equals(prefix)) {
            player.sendMessage(prefix);
        }

        Component yes = Component.empty()
                .append(Component.text("[ "))
                .append(msgService.message("api.website.registration.buttons.confirm", false))
                .append(Component.text(" ]"))
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.callback((Audience audience) -> handleYesNoClick(player.getUniqueId(), true)));

        Component no = Component.empty()
                .append(Component.text("[ "))
                .append(msgService.message("api.website.registration.buttons.deny", false))
                .append(Component.text(" ]"))
                .color(NamedTextColor.RED)
                .clickEvent(ClickEvent.callback((Audience audience) -> handleYesNoClick(player.getUniqueId(), false)));

        Component spacer = Component.text("  ");
        player.sendMessage(yes
                .append(spacer)
                .append(no)
                .append(Component.text("\n"))
        );

        if (!Component.empty().equals(suffix)) {
            msgService.send(player, "global.backlines.x1", false);
        }

        if (separator) {
            msgService.send(player, "global.separator", false);
        }
    }

    private void handleYesNoClick(UUID playerId, boolean isYes) {
        Consumer<Boolean> callback = yesNoWaiting.remove(playerId);

        if (callback == null) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(isYes));
    }

    public boolean isWaiting(Player player) {
        return waiting.containsKey(player.getUniqueId());
    }
}
