package io.realmit.edwige.api.controllers;

import io.realmit.edwige.api.dto.responses.ServerStatsResponse;
import io.realmit.edwige.api.services.ServerStatsService;

final public class ServerStatsController {

    final private ServerStatsService service;

    public ServerStatsController(ServerStatsService service) {
        this.service = service;
    }

    public ServerStatsResponse handle() {
        return new ServerStatsResponse(
                service.getOnlineCount(),
                service.getOfflineCount(),
                service.getMaxPlayers(),
                service.isServerFull(),
                service.getSerializedOnlinePlayerNames(),
                service.getSerializedOfflinePlayerNames()
        );
    }
}
