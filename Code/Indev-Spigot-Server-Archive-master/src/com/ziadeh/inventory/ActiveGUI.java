package com.ziadeh.inventory;

/**
 * Created by Brennan on 11/3/2019.
 */

import com.ziadeh.FirefightAlpha;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public abstract class ActiveGUI implements Listener {

    public ActiveGUI() {
        FirefightAlpha.getInstance().registerListener(this);
    }

    /**
     * Activates when the inventory is clicked.
     *
     * @param event InventoryClickEvent.
     */
    @EventHandler
    public abstract void onClick(InventoryClickEvent event);

    /**
     * Activates when the inventory is closed.
     *
     * @param event InventoryCloseEvent.
     */
    @EventHandler
    public abstract void onClose(InventoryCloseEvent event);

    /**
     * Gets the inventory associated with this GUI.
     *
     * @return the inventory.
     */
    public abstract Inventory get();
}
