package io.realmit.edwige.api.dto.requests.console;

import io.realmit.edwige.api.dto.requests.console.enums.OnExpireEnum;
import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface2;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ConsoleCommandRequest(
        List<CommandRequest> commands,
        @Nullable OnExpireEnum onExpire,
        @Nullable Integer ttlSeconds
) implements RequestInterface2<ConsoleCommandRequest> {}
