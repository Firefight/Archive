package com.ziadeh.inventory;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.Configuration;
import com.ziadeh.config.TL;
import network.aeternum.ordnance.core.Ordnance;
import network.aeternum.ordnance.weapons.Weapon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Brennan on 11/2/2019.
 */
public class ShopGUI extends ActiveGUI {

    private Inventory inventory;

    public ShopGUI() {
        inventory = Bukkit.createInventory(null, 27, TL.SHOP_TITLE);

        addWeapons();
    }

    private void addWeapons() {
        Collection<Weapon> weapons = Ordnance.getWeapons().values();

        weapons.forEach(weapon -> {

            ItemStack item = weapon.getItem();

            ItemMeta meta = item.getItemMeta();

            meta.setLore(new ArrayList<String>() {
                {
                    add(String.format("§a%d Credits", Configuration.getPrice(weapon.getName())));
                }
            });

            item.setItemMeta(meta);

            if(inventory.firstEmpty() != -1) inventory.addItem(item);
        });
    }

    public Inventory get() {
        return inventory;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();

        if(clicked != null && clicked.equals(event.getClickedInventory())) {
            event.setCancelled(true);

            // Ensures the player is not clicking inside their inventory, but the shop.
            if(event.getRawSlot() != event.getSlot())
                return;

            ItemStack item;
            if((item = event.getCurrentItem()) != null) {

                event.getWhoClicked().openInventory(new ConfirmationGUI(item).get());

                FirefightAlpha.getInstance().unregisterListener(this);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(event.getInventory().equals(inventory)) {
            FirefightAlpha.getInstance().unregisterListener(this);
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if(inventory.getViewers().contains(event.getPlayer())) {
            FirefightAlpha.getInstance().unregisterListener(this);
        }
    }
}
