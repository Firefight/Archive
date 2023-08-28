package com.ziadeh.commands;

import com.ziadeh.FirefightAlpha;
import com.ziadeh.config.TL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Brennan on 10/29/2019.
 */

public abstract class FCommand implements CommandExecutor {

    private String permission;

    public FCommand(String command, String permission) {
        this.permission = permission;

        FirefightAlpha.getInstance().getCommand(command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(TL.ONLY_PLAYERS);
            return false;
        }

        Player player = (Player) commandSender;

        if(!player.hasPermission(permission) && !player.isOp()) {
            player.sendMessage(TL.NO_PERMISSION);
            return false;
        }

        execute((Player) commandSender, label, args);
        return true;
    }

    /**
     * Executes the command.
     *
     * @param player the player executing the command
     * @param label the command label
     * @param args the arguments
     */
    public abstract void execute(Player player, String label, String[] args);

    /**
     * Get the permission node required to execute this command.
     *
     * @return required permission node, null if none required.
     */
    public String getPermission() {
        return permission;
    }
}
