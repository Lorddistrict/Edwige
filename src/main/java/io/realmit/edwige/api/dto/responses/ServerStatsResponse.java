package io.realmit.edwige.api.dto.responses;

public record ServerStatsResponse (
    int onlineCount,
    int offlineCount,
    int maxPlayers,
    boolean serverFull,
    String onlinePlayers,
    String offlinePlayers
) {}
