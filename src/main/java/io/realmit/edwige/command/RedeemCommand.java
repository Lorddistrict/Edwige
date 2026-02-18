package io.realmit.edwige.command;

import io.realmit.edwige.api.service.PendingItemStoreService;
import io.realmit.edwige.services.MessageService;
import io.realmit.edwige.services.PlayerActionsService;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

final public class RedeemCommand implements CommandExecutor {

    final private MessageService messageService;
    final private PendingItemStoreService pendingItemStoreService;
    final private PlayerActionsService playerActionsService;

    public RedeemCommand(
            MessageService messageService,
            PlayerActionsService playerActionsService,
            PendingItemStoreService pendingItemStoreService
    ) {
        this.messageService = messageService;
        this.playerActionsService = playerActionsService;
        this.pendingItemStoreService = pendingItemStoreService;
    }

    @Override
    public boolean onCommand(
            @NonNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NonNull [] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!playerActionsService.canPlayerRedeemItems(player)) {
            int requiredInventorySize = pendingItemStoreService.getPendingItems(player.getUniqueId()).size();
            String stringifiedSize = String.valueOf(requiredInventorySize);
            messageService.send(
                    player,
                    "redeem-error-missing-space",
                    "<requiredInventorySize>",
                    stringifiedSize
            );

            int availablePlayerInventorySlots = playerActionsService.getPlayerAvailableSlots(player);
            stringifiedSize = String.valueOf(availablePlayerInventorySlots);
            messageService.send(
                    player,
                    "redeem-error-available-slots",
                    "<availablePlayerInventorySlots>",
                    stringifiedSize
            );

            return true;
        }

        List<ItemStack> items = pendingItemStoreService.consumePendingItems(player.getUniqueId());

        if (items.isEmpty()) {
            messageService.send(player, "redeem-success-no-items");
            return true;
        }

        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }

        messageService.send(player, "redeem-success");

        return true;
    }
}
