package io.realmit.edwige.listeners.mailbox;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.menu.mailbox.MailBoxMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class MailBoxListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof MailBoxMenu menu)) return;

        PluginContext context = EdwigePlugin.getPlugin().getContext();
        if (!context.hasMenuOpen(player.getUniqueId(), "mailbox")) return;

        menu.save(player, event.getInventory());
        context.unregisterOpenMenu(player.getUniqueId());
    }
}