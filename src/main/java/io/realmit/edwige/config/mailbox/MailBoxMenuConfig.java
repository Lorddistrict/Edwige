package io.realmit.edwige.config.mailbox;

import io.realmit.edwige.EdwigePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MailBoxMenuConfig {

    public static final String MENU_CONFIG_FILE_NAME = "mailboxes.yml";

    private File file;
    private FileConfiguration config;

    public MailBoxMenuConfig() {
        createAndLoad();
    }

    public void createAndLoad() {
        Plugin plugin = EdwigePlugin.getPlugin();
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
            Plugin plugin = EdwigePlugin.getPlugin();
            plugin.getLogger().severe("Could not save " + MENU_CONFIG_FILE_NAME + ": " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
