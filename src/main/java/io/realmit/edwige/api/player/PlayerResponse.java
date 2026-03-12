package io.realmit.edwige.api.player;

import io.realmit.edwige.api.dto.responses.interfaces.ResponseInterface;

public record PlayerResponse(
    String uuid,
    String username,
    boolean isOnline,
    String displayName,
    String ip,
    int ping,
    String world,
    double balance,
    long firstPlayed,
    long lastPlayed,
    boolean isBanned,
    boolean isOp
) implements ResponseInterface<PlayerResponse> {}
