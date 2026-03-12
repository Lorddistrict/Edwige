package io.realmit.edwige.api.player;

import io.realmit.edwige.api.services.interfaces.ResponseBuilderServiceInterface;
import net.milkbowl.vault.economy.Economy;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.net.InetSocketAddress;

public class PlayerService implements ResponseBuilderServiceInterface<PlayerResponse> {

    private Economy econ = null;
    private final Plugin plugin;

    public PlayerService(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PlayerResponse buildResponse() {
        throw new UnsupportedOperationException(this.getClass() + " requires a Player");
    }

    public PlayerResponse buildResponse(Player player) {
        return new PlayerResponse(
                getPlayerUsername(player),
                getPlayerUuid(player),
                isPlayerOnline(player),
                getDisplayName(player),
                getPlayerIp(player),
                getPlayerPing(player),
                getPlayerCurrentWorld(player),
                getPlayerBalance(player),
                getPlayerFirstPlayed(player),
                getPlayerLastSeen(player),
                isPlayerBanned(player),
                isPlayerOp(player)
        );
    }

    private String getPlayerUsername(Player player) {
        return player.getName();
    }

    private String getPlayerUuid(Player player) {
        return player.getUniqueId().toString();
    }

    private boolean isPlayerOnline(Player player) {
        return player.isOnline();
    }

    private String getDisplayName(Player player) {
        return PlainTextComponentSerializer.plainText().serialize(player.displayName());
    }

    private String getPlayerIp(Player player) {
        InetSocketAddress playerAddress = player.getAddress();

        if (playerAddress == null) {
            return "";
        }

        return playerAddress.getAddress().getHostAddress();
    }

    private int getPlayerPing(Player player) {
        return player.getPing();
    }

    private String getPlayerCurrentWorld(Player player) {
        return player.getWorld().getName();
    }

    private double getPlayerBalance(Player player) {
        initVault();

        if (econ == null) {
            return 0.0;
        }

        return econ.getBalance(player);
    }

    private long getPlayerFirstPlayed(Player player) {
        return player.getFirstPlayed();
    }

    private long getPlayerLastSeen(Player player) {
        return player.getLastSeen();
    }

    private boolean isPlayerBanned(Player player) {
        return player.isBanned();
    }

    private boolean isPlayerOp(Player player) {
        return player.isOp();
    }

    private void initVault() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return;
        }

        econ = rsp.getProvider();
    }
}
