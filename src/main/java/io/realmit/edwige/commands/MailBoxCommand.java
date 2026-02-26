package io.realmit.edwige.commands;

import io.realmit.edwige.menu.interfaces.MenuInterface;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MailBoxCommand implements CommandExecutor {

    private final MenuInterface menu;

    public MailBoxCommand(MenuInterface menu) {
        this.menu = menu;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            String @NotNull [] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("edwige.mailbox")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        menu.open(player);
        return true;
    }
}
