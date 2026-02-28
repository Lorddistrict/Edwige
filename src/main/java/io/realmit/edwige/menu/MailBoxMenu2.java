package io.realmit.edwige.menu;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MailBoxMenu2 extends PaginatedMenu {

    private final Set<UUID> openedMailboxes = new HashSet<>();

    public MailBoxMenu2(PlayerMenuUtils playerMenuUtils) {
        super(playerMenuUtils);
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            event.setCancelled(true);
            return;
        }

        if (clickedItem.equals(getNext())) {
            event.setCancelled(true);
            int maxPages = EdwigePlugin
                    .getPlugin()
                    .getConfig()
                    .getInt("modules.mailbox.config.menus.mailboxMenu2.maxPages");

            if (page >= maxPages) {
                event.getWhoClicked().sendMessage("You are already on the last page.");
                return;
            }

            page = page + 1;
            super.open((Player) event.getWhoClicked());
            event.getWhoClicked().sendMessage("page = " + page);
        }

        if (clickedItem.equals(getPrevious())) {
            event.setCancelled(true);

            if (page == 0) {
                event.getWhoClicked().sendMessage("You are already on the first page.");
                return;
            }

            page = page - 1;
            super.open((Player) event.getWhoClicked());
            event.getWhoClicked().sendMessage("page = " + page);
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

        openedMailboxes.add(player.getUniqueId());
    }

    public void save(Player player, Inventory inventory) {
        FileConfiguration cfg = EdwigePlugin.getPlugin().getContext().getMailboxesConfig().getConfig();
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

        EdwigePlugin.getPlugin().getContext().getMailboxesConfig().save();
    }

    public boolean hasMailboxOpen(UUID playerId) {
        return openedMailboxes.contains(playerId);
    }

    public void markMailboxClosed(UUID playerId) {
        openedMailboxes.remove(playerId);
    }
}
