package io.realmit.edwige.serializer;

import io.realmit.edwige.api.dto.requests.console.CommandRequest;
import io.realmit.edwige.api.dto.requests.console.enums.RunAsEnum;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("CommandSerializer")
public class CommandSerializer implements ConfigurationSerializable {

    private final String command;
    private final @Nullable RunAsEnum runAs;
    private final @Nullable UUID targetPlayer;
    private final Boolean waitForPlayer;

    public CommandSerializer(
            String command,
            @Nullable RunAsEnum runAs,
            @Nullable UUID targetPlayer,
            Boolean waitForPlayer
    ) {
        this.command = command;
        this.runAs = runAs;
        this.targetPlayer = targetPlayer;
        this.waitForPlayer = waitForPlayer;
    }

    public static CommandSerializer fromRequest(CommandRequest request) {
        return new CommandSerializer(
                request.command(),
                request.runAs(),
                request.targetPlayer(),
                request.waitForPlayer()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("command", command);

        if (runAs != null) {
            map.put("runAs", runAs.name());
        }

        if (targetPlayer != null) {
            map.put("targetPlayer", targetPlayer.toString());
        }

        if (waitForPlayer != null) {
            map.put("waitForPlayer", waitForPlayer);
        }

        return map;
    }

    public static CommandSerializer deserialize(Map<String, Object> map) {
        String command = (String) map.get("command");
        String runAsRaw = (String) map.get("runAs");
        String uuidRaw = (String) map.get("targetPlayer");
        Boolean waitForPlayer = (Boolean) map.get("waitForPlayer");

        RunAsEnum runAs = null;
        if (runAsRaw != null) {
            try {
                runAs = RunAsEnum.valueOf(runAsRaw);
            } catch (IllegalArgumentException ignored) {
                // todo something ?
            }
        }

        UUID targetPlayer = null;
        if (uuidRaw != null) {
            try {
                targetPlayer = UUID.fromString(uuidRaw);
            } catch (IllegalArgumentException ignored) {
                // todo something ?
            }
        }

        return new CommandSerializer(command, runAs, targetPlayer, waitForPlayer);
    }

    public String command() {
        return command;
    }

    public @Nullable RunAsEnum runAs() {
        return runAs;
    }

    public @Nullable UUID targetPlayer() {
        return targetPlayer;
    }

    public @Nullable Boolean waitForPlayer() {
        return waitForPlayer;
    }
}
