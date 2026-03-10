package io.realmit.edwige.api.services;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.api.dto.requests.console.CommandRequest;
import io.realmit.edwige.api.dto.requests.console.ConsoleCommandRequest;
import io.realmit.edwige.api.dto.requests.console.enums.RunAsEnum;
import io.realmit.edwige.api.services.interfaces.RequestHandlerServiceInterface;
import io.realmit.edwige.serializer.CommandSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConsoleCommandService implements RequestHandlerServiceInterface<ConsoleCommandRequest> {

    @Override
    public void handleRequest(ConsoleCommandRequest consoleCommandRequest) {
        if (consoleCommandRequest == null) return;

        List<CommandRequest> commandRequestList = consoleCommandRequest.commands();

        for (CommandRequest commandRequest : commandRequestList) {
            if (commandRequest.targetPlayer() == null) {
                handleDirectCommand(commandRequest);
                continue;
            }

            if (Bukkit.getPlayer(commandRequest.targetPlayer()) == null) {
                handleOfflinePlayer(commandRequest);
                continue;
            }

            handleOnlinePlayer(commandRequest);
        }
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

    private void handleOnlinePlayer(CommandRequest request) {
        CommandSender sender = resolveSender(request.runAs(), request.targetPlayer());
        if (sender == null) return;

        Bukkit.getScheduler().runTask(
                EdwigePlugin.getPlugin(), () -> Bukkit.dispatchCommand(sender, request.command())
        );
    }

    private @Nullable CommandSender resolveSender(RunAsEnum runAs, UUID targetPlayer) {
        if (runAs == RunAsEnum.PLAYER) {
            return Bukkit.getPlayer(targetPlayer);
        }

        return Bukkit.getConsoleSender();
    }
}
