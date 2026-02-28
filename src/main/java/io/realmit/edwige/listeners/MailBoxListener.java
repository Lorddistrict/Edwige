package io.realmit.edwige.listeners;

import io.realmit.edwige.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MailBoxListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!(event.getInventory().getHolder() instanceof Menu menu)) return;
        if (event.getCurrentItem() == null) return;
        menu.handleMenu(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
//        if (!(event.getPlayer() instanceof Player player)) {
//            return;
//        }

//        UUID playerId = player.getUniqueId();

//        if (!menu.hasMailboxOpen(playerId)) {
//            return;
//        }
//
//        menu.save(player, event.getInventory());
//        menu.markMailboxClosed(playerId);
    }
}
