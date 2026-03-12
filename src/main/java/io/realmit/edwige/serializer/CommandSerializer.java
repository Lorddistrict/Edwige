package io.realmit.edwige.serializer;

import io.realmit.edwige.api.console.CommandRequest;
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
    private final @Nullable UUID targetPlayer;
    private final Boolean waitForPlayer;

    public CommandSerializer(
            String command,
            @Nullable UUID targetPlayer,
            Boolean waitForPlayer
    ) {
        this.command = command;
        this.targetPlayer = targetPlayer;
        this.waitForPlayer = waitForPlayer;
    }

    public static CommandSerializer fromRequest(CommandRequest request) {
        return new CommandSerializer(
                request.command(),
                request.targetPlayer(),
                request.waitForPlayer()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("command", command);

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
        String uuidRaw = (String) map.get("targetPlayer");
        Boolean waitForPlayer = (Boolean) map.get("waitForPlayer");
        UUID targetPlayer = null;

        if (uuidRaw != null) {
            try {
                targetPlayer = UUID.fromString(uuidRaw);
            } catch (IllegalArgumentException ignored) {
                // todo something ?
            }
        }

        return new CommandSerializer(command, targetPlayer, waitForPlayer);
    }

    public String command() {
        return command;
    }

    public @Nullable UUID targetPlayer() {
        return targetPlayer;
    }

    public @Nullable Boolean waitForPlayer() {
        return waitForPlayer;
    }
}
