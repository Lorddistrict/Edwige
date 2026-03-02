package io.realmit.edwige.listeners;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.menu.MailBoxMenu2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryHolder;

public class MailBoxListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        handleMailboxSave(event, player);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        handleMailboxSave(event, player);
        EdwigePlugin.getPlugin().getContext().unregisterOpenMenu(player.getUniqueId());
    }

    private void handleMailboxSave(InventoryEvent event, Player player) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof MailBoxMenu2 menu)) return;
        if (!EdwigePlugin.getPlugin().getContext().hasMenuOpen(player.getUniqueId(), "mailbox")) return;
        menu.save(player, event.getInventory());
    }
}