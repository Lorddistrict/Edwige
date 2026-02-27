//package io.realmit.edwige.listeners;
//
//import io.realmit.edwige.menu.MailBoxMenu;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.InventoryCloseEvent;
//
//import java.util.UUID;
//
//public class MailBoxListener implements Listener {
//
//    private final MailBoxMenu menu;
//
//    public MailBoxListener(MailBoxMenu menu) {
//        this.menu = menu;
//    }
//
//    @EventHandler
//    public void onInventoryClose(InventoryCloseEvent event) {
//        if (!(event.getPlayer() instanceof Player player)) {
//            return;
//        }
//
//        UUID playerId = player.getUniqueId();
//
//        if (!menu.hasMailboxOpen(playerId)) {
//            return;
//        }
//
//        menu.save(player, event.getInventory());
//        menu.markMailboxClosed(playerId);
//    }
//}
