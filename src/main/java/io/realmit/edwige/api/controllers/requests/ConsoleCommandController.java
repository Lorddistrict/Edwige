package io.realmit.edwige.api.controllers.requests;

import io.realmit.edwige.api.dto.requests.ConsoleCommandRequest;
import io.realmit.edwige.api.services.ConsoleCommandService;

final public class ConsoleCommandController {

    final private ConsoleCommandService service;

    public ConsoleCommandController(ConsoleCommandService service) {
        this.service = service;
    }

    public void handleRequest(ConsoleCommandRequest request) {
        service.executeRequest(request);
    }
}
