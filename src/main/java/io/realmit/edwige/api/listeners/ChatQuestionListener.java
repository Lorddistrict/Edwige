package io.realmit.edwige.api.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.realmit.edwige.services.ChatQuestionService;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ChatQuestionListener implements Listener {

    private final ChatQuestionService service;

    public ChatQuestionListener(
            ChatQuestionService service
    ) {
        this.service = service;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (!service.isWaiting(player)) {
            return;
        }

        String msg = PlainTextComponentSerializer.plainText().serialize(event.message());
        event.setCancelled(true);
        service.handlePlayerChat(player, msg);
    }
}
