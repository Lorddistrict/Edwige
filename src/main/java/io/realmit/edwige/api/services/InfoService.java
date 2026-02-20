package io.realmit.edwige.api.services;

import io.realmit.edwige.api.dto.responses.InfoResponse;
import io.realmit.edwige.api.services.interfaces.ResponseBuilderServiceInterface;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InfoService implements ResponseBuilderServiceInterface<InfoResponse> {

    public InfoService(
    ) {
    }

    @Override
    public InfoResponse buildResponse() {
        return new InfoResponse(
                getOnlineCount(),
                getOfflineCount(),
                getMaxPlayers(),
                isServerFull(),
                getServerVersion(),
                getSerializedOnlinePlayerNames(),
                getSerializedOfflinePlayerNames()
        );
    }

    private int getOnlineCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    private int getOfflineCount() {
        return Bukkit.getOfflinePlayers().length - Bukkit.getOnlinePlayers().size();
    }

    private int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    private boolean isServerFull() {
        return getOnlineCount() >= getMaxPlayers();
    }

    private Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    private List<String> getOnlinePlayerNames() {
        return getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .toList();
    }

    private String getSerializedOnlinePlayerNames() {
        return getOnlinePlayerNames()
                .stream()
                .map(name -> "\"" + name.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private OfflinePlayer[] getOfflinePlayers() {
        return Bukkit.getOfflinePlayers();
    }

    private List<String> getOfflinePlayerNames() {
        List<String> offlinePlayerNames = Arrays.stream(getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        offlinePlayerNames.removeAll(getOnlinePlayerNames());

        return offlinePlayerNames;
    }

    private String getSerializedOfflinePlayerNames() {
        return getOfflinePlayerNames()
                .stream()
                .map(name -> "\"" + name.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String getServerVersion() {
        return Bukkit.getVersion();
    }
}
