package io.realmit.edwige.menu;

import io.realmit.edwige.config.MailBoxMenuConfig;
import io.realmit.edwige.menu.interfaces.MenuInterface;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class MailBoxMenu implements MenuInterface {

    private static final int MAILBOX_SIZE = 54;

    private final Plugin plugin;
    private final MailBoxMenuConfig mailboxesConfig;
    private final Set<UUID> openedMailboxes = new HashSet<>();

    public MailBoxMenu(
            Plugin plugin,
            MailBoxMenuConfig mailboxesConfig
    ) {
        this.plugin = plugin;
        this.mailboxesConfig = mailboxesConfig;
    }

    @Override
    public void open(Player player) {
        FileConfiguration cfg = mailboxesConfig.getConfig();
        String itemsPath = "mailboxes." + player.getUniqueId() + ".items";
        String mailboxInventoryBase = plugin.getConfig().getString("modules.mailbox.config.inventory.base");

        Inventory inventory = Bukkit.createInventory(
                null,
                MAILBOX_SIZE,
                Component.text( mailboxInventoryBase + player.getName())
        );

        ConfigurationSection itemsSection = cfg.getConfigurationSection(itemsPath);

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                int slot;

                try {
                    slot = Integer.parseInt(key);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (slot < 0 || slot >= MAILBOX_SIZE) {
                    continue;
                }

                ItemStack item = itemsSection.getItemStack(key);

                if (item != null && item.getType() != Material.AIR) {
                    inventory.setItem(slot, item);
                }
            }
        }

        openedMailboxes.add(player.getUniqueId());

        player.openInventory(inventory);
    }

    public void save(Player player, Inventory inventory) {
        FileConfiguration cfg = mailboxesConfig.getConfig();
        String basePath = "mailboxes." + player.getUniqueId();

        cfg.set(basePath + ".playerName", player.getName());

        String itemsPath = basePath + ".items";
        cfg.set(itemsPath, null);

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            cfg.set(itemsPath + "." + slot, item);
        }

        mailboxesConfig.save();
    }

    public boolean hasMailboxOpen(UUID playerId) {
        return openedMailboxes.contains(playerId);
    }

    public void markMailboxClosed(UUID playerId) {
        openedMailboxes.remove(playerId);
    }
}
