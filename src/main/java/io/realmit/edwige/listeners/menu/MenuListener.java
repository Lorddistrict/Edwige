package io.realmit.edwige.listeners.menu;

import io.realmit.edwige.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener  {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!(event.getInventory().getHolder() instanceof Menu menu)) return;
        if (event.getCurrentItem() == null) return;
        menu.handleMenu(event);
    }
}
