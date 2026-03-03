package io.realmit.edwige.menu.utils;

import org.bukkit.entity.Player;

public class MenuUtils {

    Player owner;

    public MenuUtils(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
