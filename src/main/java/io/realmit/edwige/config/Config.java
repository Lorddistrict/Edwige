package io.realmit.edwige.config;

import io.realmit.edwige.EdwigePlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Config {

    protected FileConfiguration config;
    protected File file;

    public Config() {
        createAndLoad();
    }

    public abstract String getConfigFileName();

    public void createAndLoad() {
        EdwigePlugin plugin = EdwigePlugin.getPlugin();
        File dataFolder = plugin.getDataFolder();

        file = new File(dataFolder, getConfigFileName());

        if (!file.exists()) {
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                plugin.getLogger().severe("Could not create " + dataFolder + " folder");
                return;
            }

            try {
                boolean isFileCreated = file.createNewFile();
                if (!isFileCreated) {
                    plugin.getLogger().severe("Could not create " + getConfigFileName() + " file");
                    return;
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create " + getConfigFileName() + ": " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        EdwigePlugin.getPlugin().getLogger().info("Loaded " + getConfigFileName());
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            EdwigePlugin plugin = EdwigePlugin.getPlugin();
            plugin.getLogger().severe("Could not save " + getConfigFileName() + ": " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
