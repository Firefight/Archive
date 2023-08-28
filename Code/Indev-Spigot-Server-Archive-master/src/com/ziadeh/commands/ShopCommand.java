package com.ziadeh.commands;

import com.ziadeh.inventory.ShopGUI;
import org.bukkit.entity.Player;

/**
 * Created by Brennan on 11/1/2019.
 */
public class ShopCommand extends FCommand {
    public ShopCommand(String command, String permission) {
        super(command, permission);
    }

    @Override
    public void execute(Player player, String label, String[] args) {
        if(args.length == 0) {
            player.openInventory(new ShopGUI().get());
        }
    }
}
