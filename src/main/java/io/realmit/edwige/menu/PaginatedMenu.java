package io.realmit.edwige.menu;

import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;

    protected int maxItemsPerPage = 52;

    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtils playerMenuUtils) {
        super(playerMenuUtils);
    }

    public void addOptions() {
        addPreviousButton();
        addNextButton();
    }

    public void addPreviousButton() {
        ItemStack previous = new ItemStack(Material.RED_DYE, 1);
        ItemMeta meta = previous.getItemMeta();
        meta.displayName(Component.text("Previous", NamedTextColor.RED));
        previous.setItemMeta(meta);

        inventory.setItem(52, previous);
    }

    public void addNextButton() {
        ItemStack next = new ItemStack(Material.LIME_DYE, 1);
        ItemMeta meta = next.getItemMeta();
        meta.displayName(Component.text("Next", NamedTextColor.GREEN));
        next.setItemMeta(meta);

        inventory.setItem(53, next);
    }
}
