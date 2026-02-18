package io.realmit.edwige.api.service;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

final public class PendingItemStoreService {

    final private Plugin plugin;
    final private File file;
    final private Map<UUID, List<ItemStack>> pending = new HashMap<>();

    public PendingItemStoreService(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "pending-items.yml");
        loadFromDisk();
    }

    public synchronized void addPendingItem(UUID playerId, ItemStack item) {
        pending.computeIfAbsent(playerId, k -> new ArrayList<>()).add(item);
        saveToDisk();
    }

    public synchronized List<ItemStack> getPendingItems(UUID playerId) {
        List<ItemStack> items = pending.get(playerId);

        if (null == items || items.isEmpty()) {
            plugin.getLogger().info("[getPendingItems] No pending items for " + playerId);

            return Collections.emptyList();
        }

        plugin.getLogger().info("[getPendingItems] Pending items for " + playerId + ":");

        return new ArrayList<>(items);
    }

    public synchronized List<ItemStack> consumePendingItems(UUID playerId) {
        List<ItemStack> items = pending.remove(playerId);

        if (items == null) {
            return Collections.emptyList();
        }

        saveToDisk();

        return items;
    }

    private void loadFromDisk() {
        if (!file.exists()) {
            return;
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (String key : cfg.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);

                List<ItemStack> items = cfg.getList(key, Collections.emptyList())
                        .stream()
                        .filter(o -> o instanceof ItemStack)
                        .map(o -> (ItemStack) o)
                        .toList();

                if (!items.isEmpty()) {
                    pending.put(uuid, new ArrayList<>(items));
                }
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("[loadFromDisk] Invalid UUID in pending-items.yml: " + key);
            }
        }
    }

    private void saveToDisk() {
        FileConfiguration cfg = new YamlConfiguration();

        for (Map.Entry<UUID, List<ItemStack>> entry : pending.entrySet()) {
            cfg.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("[saveToDisk] Failed to save pending-items.yml: " + e.getMessage());
        }
    }
}
