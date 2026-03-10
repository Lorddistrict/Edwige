package io.realmit.edwige.api.dto.requests.console;

import io.realmit.edwige.api.dto.requests.console.enums.OnExpire;
import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ConsoleCommandRequest(
        List<CommandRequest> commands,
        @Nullable OnExpire onExpire,
        @Nullable Integer ttlSeconds
) implements RequestInterface<ConsoleCommandRequest> {}
