package studio.archetype.firefight.cardinal.server.lobby.command;

import studio.archetype.firefight.cardinal.server.item.Loadout;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestWeapons implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            Loadout.testLoadout.apply(player);
            sender.sendMessage(ChatColor.GREEN + "Applied the testing loadout.");
        } else sender.sendMessage(ChatColor.RED + "You must be in game to run this command.");
        return true;
    }
}
