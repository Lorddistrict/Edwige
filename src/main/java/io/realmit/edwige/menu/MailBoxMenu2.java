package io.realmit.edwige.menu;

import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class MailBoxMenu2 extends PaginatedMenu {

    public MailBoxMenu2(PlayerMenuUtils playerMenuUtils) {
        super(playerMenuUtils);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

    }

    @Override
    public int getInventorySize() {
        return 54;
    }

    @Override
    public Component getInventoryTitle() {
        return Component.text("Mailbox of ...", NamedTextColor.GOLD);
    }

    @Override
    public void setMenuItems() {

    }
}
