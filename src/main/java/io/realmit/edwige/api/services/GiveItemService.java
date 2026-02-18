package io.realmit.edwige.api.services;

import io.realmit.edwige.api.dto.requests.GiveItemRequest;
import io.realmit.edwige.services.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.UUID;

final public class GiveItemService {

    final private MessageService messageService;
    final private PendingItemStoreService pendingItemStoreService;
    final private Plugin plugin;

    public GiveItemService(
            MessageService messageService,
            PendingItemStoreService pendingItemStoreService,
            Plugin plugin
    ) {
        this.messageService = messageService;
        this.pendingItemStoreService = pendingItemStoreService;
        this.plugin = plugin;
    }

    public void giveItem(GiveItemRequest request) {
        String playerName = request.player();
        String itemName = request.item();
        int amount = request.amount();

        Material material = Material.matchMaterial(itemName.toUpperCase(Locale.ROOT));

        if (material == null) {
            plugin.getLogger().warning("[giveItem] Unknown item in API request: " + itemName);

            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            Player target = Bukkit.getPlayerExact(playerName);
            ItemStack item = new ItemStack(material, amount);

            if (null != target) {
                handleOnlinePlayer(target, item);
            } else {
                handleOfflinePlayer(playerName, item, material, amount);
            }
        });
    }

    private void handleOnlinePlayer(Player player, ItemStack item) {
        player.getInventory().addItem(item);

        messageService.send(player, "on-api-call-give-success");
    }

    private void handleOfflinePlayer(String playerName, ItemStack item, Material material, int amount) {
        UUID uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();

        pendingItemStoreService.addPendingItem(uuid, item);

        plugin.getLogger().info(
                "[handleOfflinePlayer] : " + amount + " " + material.name() + " for offline player " + playerName
        );
    }
}
