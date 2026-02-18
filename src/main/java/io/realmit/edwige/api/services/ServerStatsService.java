package io.realmit.edwige.api.services;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServerStatsService {

    public ServerStatsService(
    ) {
    }

    public int getOnlineCount() {
        return Bukkit.getOnlinePlayers().size();
    }

    public int getOfflineCount() {
        return Bukkit.getOfflinePlayers().length - Bukkit.getOnlinePlayers().size();
    }

    public int getMaxPlayers() {
        return Bukkit.getMaxPlayers();
    }

    public boolean isServerFull() {
        return getOnlineCount() >= getMaxPlayers();
    }

    public Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public List<String> getOnlinePlayerNames() {
        return getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .toList();
    }

    public String getSerializedOnlinePlayerNames() {
        return getOnlinePlayerNames()
                .stream()
                .map(name -> "\"" + name.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    public OfflinePlayer[] getOfflinePlayers() {
        return Bukkit.getOfflinePlayers();
    }

    public List<String> getOfflinePlayerNames() {
        List<String> offlinePlayerNames = Arrays.stream(getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        offlinePlayerNames.removeAll(getOnlinePlayerNames());

        return offlinePlayerNames;
    }

    public String getSerializedOfflinePlayerNames() {
        return getOfflinePlayerNames()
                .stream()
                .map(name -> "\"" + name.replace("\"", "\\\"") + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }
}
