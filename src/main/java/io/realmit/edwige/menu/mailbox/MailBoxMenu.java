package io.realmit.edwige.menu.mailbox;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.menu.PaginatedMenu;
import io.realmit.edwige.menu.utils.MenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public final class MailBoxMenu extends PaginatedMenu {

    public MailBoxMenu(MenuUtils menuUtils) {
        super(menuUtils);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof MailBoxMenu)) return;
        if (!context.hasMenuOpen(event.getWhoClicked().getUniqueId(), "mailbox")) return;

        FileConfiguration config = EdwigePlugin.getPlugin().getConfig();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            event.setCancelled(true);
            return;
        }

        if (clickedItem.equals(getNext())) {
            event.setCancelled(true);
            int maxPages = config.getInt("modules.mailbox.config.menus.mailboxMenu.maxPages");

            if (page >= maxPages) {
                return;
            }

            page = page + 1;
            super.open();
        }

        if (clickedItem.equals(getPrevious())) {
            event.setCancelled(true);

            if (page == 0) {
                return;
            }

            page = page - 1;
            super.open();
        }

        if (clickedItem.equals(getGrayPane())) {
            event.setCancelled(true);
        }
    }

    @Override
    public int getInventorySize() {
        return 54;
    }

    @Override
    public Component getInventoryTitle() {
        FileConfiguration config = EdwigePlugin.getPlugin().getConfig();
        int maxPages = config.getInt("modules.mailbox.config.menus.mailboxMenu.maxPages");
        int currentPage = page+1;
        String text = "Mailbox of " + menuUtils.getOwner().getName() + " - " + currentPage + "/" + maxPages;

        return Component.text(text, NamedTextColor.DARK_RED);
    }

    @Override
    public void setMenuItems() {
        String itemsPath = "mailboxes." + menuUtils.getOwner().getUniqueId() + ".pages." + page + ".items";
        PluginContext context = EdwigePlugin.getPlugin().getContext();
        FileConfiguration cfg = context.getMailboxesConfig().getConfig();
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

    public void save(Player player, Inventory inventory) {
        PluginContext context = EdwigePlugin.getPlugin().getContext();
        FileConfiguration cfg = context.getMailboxesConfig().getConfig();
        String basePath = "mailboxes." + player.getUniqueId();

        cfg.set(basePath + ".playerName", player.getName());
        String itemsPath = basePath + ".pages." + page + ".items";
        cfg.set(itemsPath, null);

        for (int slot = 0; slot < maxItemsPerPage; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            cfg.set(itemsPath + "." + slot, item);
        }

        context.getMailboxesConfig().save();
    }
}
