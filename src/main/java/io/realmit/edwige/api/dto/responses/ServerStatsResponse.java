package io.realmit.edwige.api.dto.responses;

import io.realmit.edwige.api.dto.requests.interfaces.ResponseInterface;

public record ServerStatsResponse (
    int onlineCount,
    int offlineCount,
    int maxPlayers,
    boolean serverFull,
    String onlinePlayers,
    String offlinePlayers
) implements ResponseInterface<ServerStatsResponse> {}
