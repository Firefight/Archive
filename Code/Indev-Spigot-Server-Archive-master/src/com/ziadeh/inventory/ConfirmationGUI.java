package com.ziadeh.inventory;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.Configuration;
import com.ziadeh.config.TL;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sun.security.krb5.Config;

import java.util.HashMap;

/**
 * Created by Brennan on 11/3/2019.
 */
public class ConfirmationGUI extends ActiveGUI {

    private ItemStack confirmationItem;

    private Inventory inventory;

    // Below we create our buttons. No need to create them every time, so we do it once and reuse.

    private static ItemStack declineButton;

    private static ItemStack acceptButton;

    private static ItemStack filler;

    static {
        declineButton = new FancyItem(new ItemStack(Material.STAINED_GLASS_PANE, 1,
                (short) DyeColor.RED.ordinal())).setDisplayname("§cCancel Purchase").build();

        acceptButton = new FancyItem(new ItemStack(Material.STAINED_GLASS_PANE, 1,
                (short) DyeColor.GREEN.ordinal())).setDisplayname("§aConfirm Purchase").build();

        filler = new FancyItem(new ItemStack(Material.STAINED_GLASS_PANE, 1,
                (short) DyeColor.BLACK.ordinal())).setDisplayname("§0").build();
    }

    public ConfirmationGUI(ItemStack item) {
        confirmationItem = item;

        inventory = Bukkit.createInventory(null, 27, TL.CONFIRMATION_TITLE);

        fill();
    }

    private void fill() {

        inventory.setItem(10, declineButton);

        inventory.setItem(13, confirmationItem);

        inventory.setItem(16, acceptButton);

        for(int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
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

                if(!item.isSimilar(acceptButton) && !item.isSimilar(declineButton))
                    return;

                if(item.isSimilar(acceptButton)) {

                    Player player = (Player) event.getWhoClicked();

                    String weaponName = confirmationItem.getItemMeta().getDisplayName();

                    String weaponNameStripped = ChatColor.stripColor(confirmationItem.getItemMeta().getDisplayName());

                    EconomyResponse economyResponse = FirefightAlpha.getEconomy().withdrawPlayer(player,
                            Configuration.getPrice(weaponNameStripped));

                    if(!economyResponse.transactionSuccess()){

                        player.sendMessage(TL.get(TL.NOT_ENOUGH_CREDITS, new HashMap<String, String>() {{
                            put("%item%", weaponName);
                        }}));

                    } else {

                        player.sendMessage(TL.get(TL.PURCHASE_SUCCESSFUL, new HashMap<String, String>() {{
                            put("%item%", weaponName);
                        }}));

                        ItemStack weapon = Configuration.getWeaponByName(weaponNameStripped);

                        // Check if the player's inventory is full. If it is, drop gun on floor in front of them.
                        if(player.getInventory().firstEmpty() == -1) {

                            player.getWorld().dropItemNaturally(player.getLocation(), weapon);

                        } else {

                            // If is has space, simply add the weapon...
                            player.getInventory().addItem(weapon);

                        }
                    }

                    player.closeInventory();

                } else {
                    event.getWhoClicked().openInventory(new ShopGUI().get());
                }

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

    public Inventory get() {
        return inventory;
    }
}
