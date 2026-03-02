package io.realmit.edwige.menu.utils;

import org.bukkit.entity.Player;

public class PlayerMenuUtils {

    Player owner;

    public PlayerMenuUtils(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
