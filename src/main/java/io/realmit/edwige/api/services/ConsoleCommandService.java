package io.realmit.edwige.api.services;

import io.realmit.edwige.api.dto.requests.ConsoleCommandRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

public class ConsoleCommandService {

    private final Plugin plugin;

    public ConsoleCommandService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void executeRequest(ConsoleCommandRequest request) {
        if (request == null) {
            return;
        }

        String command = request.command();

        if (command == null || command.isBlank()) {
            return;
        }

        String fullCommand = command.trim();
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String commandLine = fullCommand.startsWith("/") ? fullCommand.substring(1) : fullCommand;
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(console, commandLine));
    }
}
