package io.realmit.edwige.menu;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class MailBoxMenu2 extends PaginatedMenu {

    public MailBoxMenu2(PlayerMenuUtils playerMenuUtils) {
        super(playerMenuUtils);
    }

    @Override
    public void handleMenu() {}

    @Override
    public int getInventorySize() {
        return 54;
    }

    @Override
    public Component getInventoryTitle() {
        return Component.text("Mailbox of ...", NamedTextColor.GOLD);
    }

    @Override
    public void setMenuItems(Player player) {
        FileConfiguration cfg = EdwigePlugin.getPlugin().getContext().getMailboxesConfig().getConfig();
        String itemsPath = "mailboxes." + player.getUniqueId() + ".pages." + page + ".items";
        ConfigurationSection itemsSection = cfg.getConfigurationSection(itemsPath);

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                int slot;

                try {
                    slot = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (slot < 0 || slot >= maxItemsPerPage) continue;

                ItemStack item = itemsSection.getItemStack(key);
                if (item != null && item.getType() != Material.AIR) {
                    inventory.setItem(slot, item);
                }
            }
        }

        addPreviousButton();
        addEmptySlots();
        addNextButton();
    }
}
