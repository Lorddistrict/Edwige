package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.controllers.interfaces.RequestHandlerControllerInterface;
import io.realmit.edwige.api.dto.requests.ConsoleCommandRequest;
import io.realmit.edwige.api.services.ConsoleCommandService;

public final class ConsoleCommandController implements RequestHandlerControllerInterface<ConsoleCommandRequest> {

    private final ConsoleCommandService service;

    public ConsoleCommandController(ConsoleCommandService service) {
        this.service = service;
    }

    public void handleRequest(ConsoleCommandRequest request) {
        service.handleRequest(request);
    }
}
