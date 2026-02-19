package io.realmit.edwige.api.dto.requests;

import io.realmit.edwige.api.dto.requests.interfaces.RequestInterface;

public record ConsoleCommandRequest(
        String command
) implements RequestInterface<ConsoleCommandRequest> {}
