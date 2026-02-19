package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.controllers.interfaces.ResponseBuilderControllerInterface;
import io.realmit.edwige.api.dto.responses.ServerStatsResponse;
import io.realmit.edwige.api.services.ServerStatsService;

public final class ServerStatsController implements ResponseBuilderControllerInterface<ServerStatsResponse> {

    private final ServerStatsService service;

    public ServerStatsController(ServerStatsService service) {
        this.service = service;
    }

    public ServerStatsResponse buildResponse() {
        return service.buildResponse();
    }
}
