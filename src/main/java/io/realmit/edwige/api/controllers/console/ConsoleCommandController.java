package io.realmit.edwige.api.controllers.console;

import io.realmit.edwige.api.controllers.AbstractController;
import io.realmit.edwige.api.dto.requests.console.ConsoleCommandRequest;
import io.realmit.edwige.api.dto.responses.console.CommandResponse;
import io.realmit.edwige.api.services.ConsoleCommandService;

public final class ConsoleCommandController extends AbstractController<CommandResponse, ConsoleCommandRequest> {

    private final ConsoleCommandService service;

    public ConsoleCommandController(ConsoleCommandService service) {
        this.service = service;
    }

    @Override
    public CommandResponse buildResponse(ConsoleCommandRequest request) {
        return service.buildResponse(request);
    }
}
