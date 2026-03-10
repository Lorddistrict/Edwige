package io.realmit.edwige.services;

import io.realmit.edwige.EdwigePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public final class MessageService {

    private final MiniMessage mini = MiniMessage.miniMessage();
    private FileConfiguration config;

    public MessageService() {
        load();
    }

    public void reload() {
        load();
    }

    private void load() {
        String resourcePath = "messages.yml";
        File file = new File(EdwigePlugin.getPlugin().getDataFolder(), resourcePath);
        EdwigePlugin.getPlugin().saveResource(resourcePath, true);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String rawPrefix() {
        return config.getString("prefix", "<yellow><bold>[Edwige]</bold></yellow> ");
    }

    public String getMessageFromKey(String key) {
        return config.getString("messages." + key, "<red>Missing message key: " + key + "</red>");
    }

    public Component message(String key, boolean displayPluginName) {
        String raw = getMessageFromKey(key);

        if (displayPluginName) {
            raw = rawPrefix() + raw;
        }

        return mini.deserialize(raw);
    }

    public Component message(String key, String placeholder, String value, boolean displayPluginName) {
        String raw = rawPrefix() + getMessageFromKey(key).replace(placeholder, value);

        if (!displayPluginName) {
            raw = getMessageFromKey(key).replace(placeholder, value);
        }

        return mini.deserialize(raw);
    }

    public void clearPlayerChat(Player player) {
        for (int i = 0; i < 20; i++) {
            player.sendMessage("");
        }
    }

    public void send(Player player, String key, boolean displayPluginName) {
        player.sendMessage(message(key, displayPluginName));
    }

    public void send(Player player, String key, String placeholder, String value, boolean displayPluginName) {
        player.sendMessage(message(key, placeholder, value, displayPluginName));
    }

    public void send(CommandSender sender, String key, boolean displayPluginName) {
        sender.sendMessage(message(key, displayPluginName));
    }

    public void send(CommandSender sender, String key, String placeholder, String value, boolean displayPluginName) {
        sender.sendMessage(message(key, placeholder, value, displayPluginName));
    }
}
