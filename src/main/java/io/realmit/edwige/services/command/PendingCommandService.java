package io.realmit.edwige.services.command;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.serializer.CommandSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PendingCommandService {

    public void add(CommandSerializer commandSerializer) {
        PluginContext context = EdwigePlugin.getPlugin().getContext();
        FileConfiguration cfg = context.getCommandConfig().getConfig();
        List<CommandSerializer> commandSerializerList = new ArrayList<>();

        String path = "commands." + commandSerializer.targetPlayer();
        List<?> list = cfg.getList(path);

        if (list != null) {
            for (Object object : list) {
                if (!(object instanceof CommandSerializer command)) continue;
                commandSerializerList.add(command);
            }
        }

        commandSerializerList.add(commandSerializer);
        cfg.set(path, commandSerializerList);
        context.getCommandConfig().save();
    }

    public void execute(Player player) {
        PluginContext context = EdwigePlugin.getPlugin().getContext();
        FileConfiguration cfg = context.getCommandConfig().getConfig();

        String path = "commands." + player.getUniqueId();
        List<?> raw = cfg.getList(path);

        if (raw == null || raw.isEmpty()) return;

        List<CommandSerializer> pending = new ArrayList<>();
        for (Object obj : raw) {
            if (obj instanceof CommandSerializer cmd) {
                pending.add(cmd);
            }
        }

        for (CommandSerializer cmd : pending) {
            Bukkit.getScheduler().runTask(EdwigePlugin.getPlugin(), () ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.command())
            );
        }

        cfg.set(path, null);
        context.getCommandConfig().save();
    }
}
