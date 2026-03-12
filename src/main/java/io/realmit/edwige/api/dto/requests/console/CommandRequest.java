package io.realmit.edwige.api.dto.requests.console;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public record CommandRequest(
        String command,
        @Nullable UUID targetPlayer,
        Boolean waitForPlayer
) {}
