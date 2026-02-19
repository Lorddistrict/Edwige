package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.dto.responses.ServerStatsResponse;
import io.realmit.edwige.api.services.ServerStatsService;

public final class ServerStatsController {

    private final ServerStatsService service;

    public ServerStatsController(ServerStatsService service) {
        this.service = service;
    }

    public ServerStatsResponse handle() {
        return service.buildResponse();
    }
}
