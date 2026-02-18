package io.realmit.edwige.services;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

final public class MessageService {

    final private Plugin plugin;
    final private MiniMessage mini = MiniMessage.miniMessage();
    private FileConfiguration config;

    public MessageService(Plugin plugin) {
        this.plugin = plugin;
        load();
    }

    public void reload() {
        load();
    }

    private void load() {
        String resourcePath = "messages.yml";
        File file = new File(plugin.getDataFolder(), resourcePath);
        plugin.saveResource(resourcePath, true);
        config = YamlConfiguration.loadConfiguration(file);
    }

    private String rawPrefix() {
        return config.getString("prefix", "<yellow><bold>[Edwige]</bold></yellow> ");
    }

    private String rawMessage(String key) {
        return config.getString("messages." + key, "<red>Missing message key: " + key + "</red>");
    }

    public Component message(String key) {
        String raw = rawPrefix() + rawMessage(key);

        return mini.deserialize(raw);
    }

    public Component message(String key, String placeholder, String value) {
        String raw = rawPrefix() + rawMessage(key).replace(placeholder, value);

        return mini.deserialize(raw);
    }

    public void send(Player player, String key) {
        player.sendMessage(message(key));
    }

    public void send(Player player, String key, String placeholder, String value) {
        player.sendMessage(message(key, placeholder, value));
    }

    public void send(CommandSender sender, String key) {
        sender.sendMessage(message(key));
    }

    public void send(CommandSender sender, String key, String placeholder, String value) {
        sender.sendMessage(message(key, placeholder, value));
    }
}
