package io.realmit.edwige.serializer;

import io.realmit.edwige.api.console.CommandRequest;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

@SerializableAs("CommandSerializer")
public record CommandSerializer(String command, Boolean waitForPlayer) implements ConfigurationSerializable {

    public static CommandSerializer fromRequest(CommandRequest request) {
        return new CommandSerializer(
                request.command(),
                request.waitForPlayer()
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("command", command);

        if (waitForPlayer != null) {
            map.put("waitForPlayer", waitForPlayer);
        }

        return map;
    }

    public static CommandSerializer deserialize (Map<String, Object> map) {
        String command = (String) map.get("command");
        Boolean waitForPlayer = (Boolean) map.get("waitForPlayer");

        return new CommandSerializer(command, waitForPlayer);
    }

    @Override
    public @Nullable Boolean waitForPlayer() {
        return waitForPlayer;
    }
}
