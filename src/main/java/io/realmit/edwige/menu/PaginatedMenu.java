package io.realmit.edwige.menu;

import io.realmit.edwige.menu.utils.PlayerMenuUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;

    protected int maxItemsPerPage = 45;

    protected int index = 0;

    protected ItemStack previous;

    protected ItemStack next;

    protected ItemStack grayPane;

    public PaginatedMenu(PlayerMenuUtils playerMenuUtils) {
        super(playerMenuUtils);
    }

    public void addPreviousButton() {
        previous = new ItemStack(Material.RED_DYE, 1);
        ItemMeta meta = previous.getItemMeta();
        meta.displayName(Component.text("Previous", NamedTextColor.RED));
        previous.setItemMeta(meta);

        inventory.setItem(45, previous);
    }

    public ItemStack getPrevious() {
        return previous;
    }

    public void addNextButton() {
        next = new ItemStack(Material.LIME_DYE, 1);
        ItemMeta meta = next.getItemMeta();
        meta.displayName(Component.text("Next", NamedTextColor.GREEN));
        next.setItemMeta(meta);

        inventory.setItem(53, next);
    }

    public ItemStack getNext() {
        return next;
    }

    public void addEmptySlots() {
        grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        for (int i = 46; i < 53; i++) {
            inventory.setItem(i, grayPane);
        }
    }

    public ItemStack getGrayPane() {
        return grayPane;
    }
}
