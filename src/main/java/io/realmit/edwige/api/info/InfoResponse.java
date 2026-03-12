package io.realmit.edwige.api.info;

import io.realmit.edwige.api.dto.responses.interfaces.ResponseInterface;

public record InfoResponse(
    int onlineCount,
    int offlineCount,
    int maxPlayers,
    boolean serverFull,
    String serverVersion,
    String onlinePlayers,
    String offlinePlayers
) implements ResponseInterface<InfoResponse> {}
