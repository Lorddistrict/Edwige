package io.realmit.edwige.api.dto.responses.console;

import io.realmit.edwige.api.dto.responses.console.enums.StatusEnum;
import io.realmit.edwige.api.dto.responses.interfaces.ResponseInterface2;

import java.util.List;

public record CommandResponse(
        List<StatusEnum> statuses,
        List<String> outputs
) implements ResponseInterface2<CommandResponse> {
}
