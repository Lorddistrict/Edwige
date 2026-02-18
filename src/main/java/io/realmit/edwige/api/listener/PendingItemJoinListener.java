package io.realmit.edwige.api.listener;

import io.realmit.edwige.api.service.PendingItemStoreService;
import io.realmit.edwige.services.MessageService;
import io.realmit.edwige.services.PlayerActionsService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

final public class PendingItemJoinListener implements Listener {

    final private MessageService messageService;
    final private PendingItemStoreService pendingItemStoreService;
    final private PlayerActionsService playerActionsService;

    public PendingItemJoinListener(
            MessageService messageService,
            PendingItemStoreService pendingItemStoreService,
            PlayerActionsService playerActionsService
    ) {
        this.messageService = messageService;
        this.pendingItemStoreService = pendingItemStoreService;
        this.playerActionsService = playerActionsService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!playerActionsService.canPlayerRedeemItems(player)) {
            return;
        }

        List<ItemStack> items = pendingItemStoreService.consumePendingItems(player.getUniqueId());

        if (items.isEmpty()) {
            return;
        }

        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }

        messageService.send(
                player,
                "on-player-join-give-success",
                "<itemSize>",
                String.valueOf(items.size())
        );
    }
}
