package io.realmit.edwige.api.console;

import io.realmit.edwige.api.console.enums.OnExpireEnum;
import io.realmit.edwige.api.shared.interfaces.RequestInterface2;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ConsoleCommandRequest(
        List<CommandRequest> commands,
        @Nullable OnExpireEnum onExpire,
        @Nullable Integer ttlSeconds
) implements RequestInterface2<ConsoleCommandRequest> {}
