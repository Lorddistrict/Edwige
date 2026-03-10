package io.realmit.edwige.api.dto.requests.console;

import io.realmit.edwige.api.dto.requests.console.enums.RunAsEnum;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record CommandRequest(
        String command,
        @Nullable RunAsEnum runAs,
        @Nullable UUID targetPlayer,
        Boolean waitForPlayer
) {
    public CommandRequest {
        if (runAs == null) runAs = RunAsEnum.CONSOLE;
    }
}
