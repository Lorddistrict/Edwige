package io.realmit.edwige.menu;

import io.realmit.edwige.EdwigePlugin;
import io.realmit.edwige.PluginContext;
import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jspecify.annotations.NonNull;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected PlayerMenuUtils playerMenuUtils;
    protected final PluginContext context;

    public Menu(PlayerMenuUtils playerMenuUtils) {
        this.playerMenuUtils = playerMenuUtils;
        this.context = EdwigePlugin.getPlugin().getContext();
    }

    public abstract void handleMenu(InventoryClickEvent event);

    public abstract int getInventorySize();

    public abstract Component getInventoryTitle();

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getInventorySize(), getInventoryTitle());
        setMenuItems();
        playerMenuUtils.getOwner().openInventory(inventory);
        context.registerOpenMenu(playerMenuUtils.getOwner().getUniqueId(), "mailbox");
    }

    @NonNull @Override
    public Inventory getInventory() {
        return inventory;
    }
}
