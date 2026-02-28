package io.realmit.edwige.listeners;

import io.realmit.edwige.menu.MailBoxMenu2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.UUID;

public class MailBoxListener implements Listener {

    protected MailBoxMenu2 menu;

    public MailBoxListener(MailBoxMenu2 menu) {
        this.menu = menu;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        UUID playerId = player.getUniqueId();
        if (!menu.hasMailboxOpen(playerId)) return;
        menu.save(player, event.getInventory());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        UUID playerId = player.getUniqueId();
        if (!menu.hasMailboxOpen(playerId)) return;
        menu.save(player, event.getInventory());
        menu.markMailboxClosed(playerId);

    }

}
