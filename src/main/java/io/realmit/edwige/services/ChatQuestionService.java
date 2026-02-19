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

    private final MessageService messageService;
    private final Plugin plugin;
    private final Map<UUID, Consumer<String>> waiting = new ConcurrentHashMap<>();
    private final Map<UUID, Consumer<Boolean>> yesNoWaiting = new ConcurrentHashMap<>();

    public ChatQuestionService(
            MessageService messageService,
            Plugin plugin
    ) {
        this.messageService = messageService;
        this.plugin = plugin;
    }

    public void ask(Player player, String questionKey, Consumer<String> callback) {
        waiting.put(player.getUniqueId(), callback);
        messageService.send(player, questionKey);
    }

    public void handlePlayerChat(Player player, String message) {
        Consumer<String> callback = waiting.remove(player.getUniqueId());

        if (callback == null) {
            return;
        }

        plugin.getServer().getScheduler().runTask(plugin, () -> callback.accept(message));
    }

    public void askYesNo(Player player, String questionKey, Consumer<Boolean> callback) {
        UUID playerUniqueId = player.getUniqueId();
        yesNoWaiting.put(playerUniqueId, callback);
        messageService.send(player, questionKey);

        Component yes = Component
                .text("[YES]")
                .color(NamedTextColor.GREEN)
                .clickEvent(ClickEvent.callback((Audience audience) -> {
                    handleYesNoClick(player.getUniqueId(), true);
                }));

        Component no = Component
                .text("[NO]")
                .color(NamedTextColor.RED)
                .clickEvent(ClickEvent.callback((Audience audience) -> {
                    handleYesNoClick(player.getUniqueId(), false);
                }));

        Component spacer = Component.text(" ");
        player.sendMessage(yes.append(spacer).append(no));
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
