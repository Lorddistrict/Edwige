package io.realmit.edwige.api.console;

import io.realmit.edwige.api.console.enums.StatusEnum;
import io.realmit.edwige.api.shared.interfaces.ResponseInterface2;

import java.util.List;

public record ConsoleCommandResponse(
        List<StatusEnum> statuses,
        List<String> outputs
) implements ResponseInterface2<ConsoleCommandResponse> {
}
