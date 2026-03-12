package io.realmit.edwige.api.console;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.api.console.enums.StatusEnum;
import io.realmit.edwige.serializer.CommandSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConsoleCommandService {

    public ConsoleCommandResponse buildResponse(ConsoleCommandRequest consoleCommandRequest) {
        List<StatusEnum> statuses = new ArrayList<>();
        List<String> outputs = new ArrayList<>();

        for (CommandRequest commandRequest : consoleCommandRequest.commands()) {
            if (commandRequest.targetPlayer() == null) {
                handleDirectCommand(commandRequest);
                statuses.add(StatusEnum.SUCCESS);
                outputs.add("Command dispatched: " + commandRequest.command());
                continue;
            }

            if (Bukkit.getPlayer(commandRequest.targetPlayer()) == null) {
                handleOfflinePlayer(commandRequest);
                statuses.add(StatusEnum.QUEUED);
                outputs.add("Player offline, command queued: " + commandRequest.command());
                continue;
            }

            handleDirectCommand(commandRequest);
            statuses.add(StatusEnum.SUCCESS);
            outputs.add("Command dispatched to player: " + commandRequest.command());
        }

        return new ConsoleCommandResponse(statuses, outputs);
    }

    private void handleDirectCommand(CommandRequest request) {
        Bukkit.getScheduler().runTask(
                EdwigePlugin.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), request.command())
        );
    }

    private void handleOfflinePlayer(CommandRequest request) {
        PluginContext context = EdwigePlugin.getPlugin().getContext();
        FileConfiguration cfg = context.getCommandConfig().getConfig();

        String path = "commands." + request.targetPlayer();
        List<?> list = cfg.getList(path);
        List<CommandSerializer> commandSerializerList = new ArrayList<>();

        if (list != null) {
            for (Object object : list) {
                if (object instanceof CommandSerializer command) {
                    commandSerializerList.add(command);
                }
            }
        }

        commandSerializerList.add(CommandSerializer.fromRequest(request));
        cfg.set(path, commandSerializerList);
        context.getCommandConfig().save();
    }
}
