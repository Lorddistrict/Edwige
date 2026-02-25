package io.realmit.edwige.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MailBoxMenuConfig {

    public static final String MENU_CONFIG_FILE_NAME = "mailboxes.yml";

    private final Plugin plugin;
    private File file;
    private FileConfiguration config;

    public MailBoxMenuConfig(Plugin plugin) {
        this.plugin = plugin;
        createAndLoad();
    }

    private void createAndLoad() {
        file = new File(plugin.getDataFolder(), MENU_CONFIG_FILE_NAME);

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + MENU_CONFIG_FILE_NAME + ": " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + MENU_CONFIG_FILE_NAME + ": " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
