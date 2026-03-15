package io.realmit.edwige.listeners.player;

import io.realmit.edwige.EdwigePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        EdwigePlugin.getPlugin().getContext().getPendingCommandService().execute(event.getPlayer());
    }
}
