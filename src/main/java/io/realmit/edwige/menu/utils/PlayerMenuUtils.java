package io.realmit.edwige.menu.utils;

import org.bukkit.entity.Player;

public class PlayerMenuUtils {

    private final Player owner;

    public PlayerMenuUtils(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
