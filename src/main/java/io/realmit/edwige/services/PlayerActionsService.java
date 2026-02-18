package io.realmit.edwige.services;

import io.realmit.edwige.api.services.PendingItemStoreService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerActionsService {
    final private PendingItemStoreService pendingItemStoreService;

    public PlayerActionsService(PendingItemStoreService pendingItemStoreService) {
        this.pendingItemStoreService = pendingItemStoreService;
    }

    public int getPlayerAvailableSlots(Player player) {
        Inventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getStorageContents();

        int emptySlots = 0;

        for (ItemStack content : contents) {
            if (content == null || content.getType().isAir() || content.getAmount() <= 0) {
                emptySlots++;
            }
        }

        return emptySlots;
    }

    public boolean canPlayerRedeemItems(Player player) {
        int emptySlots = getPlayerAvailableSlots(player);
        List<ItemStack> itemsToGive = pendingItemStoreService.getPendingItems(player.getUniqueId());

        return itemsToGive.size() <= emptySlots;
    }
}
