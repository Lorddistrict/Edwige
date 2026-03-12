package io.realmit.edwige.api.console;

import io.realmit.edwige.api.shared.abstracts.AbstractController;

public final class ConsoleCommandController extends AbstractController<ConsoleCommandResponse, ConsoleCommandRequest> {

    private final ConsoleCommandService service;

    public ConsoleCommandController(ConsoleCommandService service) {
        this.service = service;
    }

    @Override
    public ConsoleCommandResponse buildResponse(ConsoleCommandRequest request) {
        return service.buildResponse(request);
    }
}
